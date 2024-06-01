import { defineComponent, provide } from "vue";
//import { useAlertService } from '@/services/alert.service';
import { RouterLink, RouterView } from "vue-router";
import TranslationService from '@/services/translation.service';
import { useI18n } from 'vue-i18n';

export default defineComponent({
  name: "App",
  components: {
    RouterLink,
    RouterView,
  },
  setup() {
    //provide('alertService', useAlertService());
    const i18n = useI18n();
    const translationService = new TranslationService(i18n);

    const changeLanguage = async (newLanguage: string) => {
      if (i18n.locale.value !== newLanguage) {
        translationService.setLocale(newLanguage);
        await translationService.refreshTranslation(newLanguage);
      }
    };
    changeLanguage('de');
    provide('translationService', translationService);
    
    console.debug("APP_INFO:", __APP_INFO__);
    console.debug("VITE_BASE_API_URL_ANGEBOT:", __VITE_BASE_API_URL_ANGEBOT__);
    
    return {};
  },
});
