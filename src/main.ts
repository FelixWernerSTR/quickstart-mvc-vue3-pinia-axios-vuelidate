import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.js";
import { createApp } from "vue";
import { createPinia, storeToRefs } from "pinia";
import App from "./App.vue";
import router from "./router";
import axios from 'axios';
import { initI18N } from './config/initI18N';

import "./assets/main.css";

const app = createApp(App);

app.use(createPinia());

app.use(router);

//sehr gute Anleitung: https://lokalise.com/blog/vue-i18n/
const i18nGlobal = initI18N();
app.use(i18nGlobal);

//https://stackoverflow.com/questions/45578844/how-to-set-header-and-options-in-axios
//https://stackoverflow.com/questions/43871637/no-access-control-allow-origin-header-is-present-on-the-requested-resource-whe
axios.interceptors.request.use(request => {
   console.debug('Starting Request', JSON.stringify(request, null, 2))
   //console.log('Starting Request', request)
    return request
  })

axios.interceptors.response.use(response => {
    console.debug('Response:', JSON.stringify(response, null, 2))
    //console.log('Response:', response)
    return response
})

app.mount("#app");
