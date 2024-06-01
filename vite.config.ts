// setup generiert von de.svi.devops:properties-to-resources:2.9.10
import { fileURLToPath, URL } from 'node:url'
import vue from '@vitejs/plugin-vue'
import { loadEnv } from 'vite';
import type { UserConfig, ConfigEnv} from 'vite';
import pkg from './package.json';
import dayjs from 'dayjs';

const CWD = process.cwd();

const __APP_INFO__ = {
  pkg,
  lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};

// https://vitejs.dev/config/
export default ({ command, mode }: ConfigEnv): UserConfig => {
  const { VITE_BASE_API_URL_ANGEBOT } = loadEnv(mode, CWD);

  const isBuild = command === 'build';

  return {
  define: {
    __APP_INFO__: JSON.stringify(__APP_INFO__),
    __VITE_BASE_API_URL_ANGEBOT__: JSON.stringify(VITE_BASE_API_URL_ANGEBOT),
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {  
    target: 'modules', //https://vitejs.dev/config/build-options#build-target
    minify: true, // damit der javascriptcode in "dist/selenium" noch lesbar(im debugger) bleibt , https://vitejs.dev/config/build-options#build-minify
    assetsInlineLimit: 4096, //https://vitejs.dev/config/build-options#build-assetsinlinelimit
  },
  server: {//ACHTUNG! diese Ausgaben sind nur auf der vite-Konsole zu sehen!
    proxy: {
      '/api/angebot': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
          proxy.on('proxyReq', (proxyReq, req, _res) => {
            console.log('Sending Request to the Target(method,host,url):', req.method, proxyReq.host, req.url);
          });
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log('Received Response from the Target(http-code,url):', proxyRes.statusCode, req.url);
          });
        }, 
      },
    }
  },
 };
};

