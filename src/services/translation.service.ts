import axios from 'axios';
import { type Composer } from 'vue-i18n';
import dayjs from 'dayjs';
import languages from '@/config/languages';

export default class TranslationService {
  private i18n: Composer;
  private languages = languages();

  constructor(i18n: Composer) {
    this.i18n = i18n;
  }

  public async refreshTranslation(newLanguage: string) {
    console.debug("refreshTranslation:",newLanguage);
    if (this.i18n && !this.i18n.messages[newLanguage]) {
      const translations = (await import(`../i18n/${newLanguage}/${newLanguage}.js`)).default;
      console.debug("translations imported:",translations);
      this.i18n.setLocaleMessage(newLanguage, translations);
    }
  }

  public setLocale(lang: string) {
    console.debug("setLocale:",lang);
    dayjs.locale(lang);
    this.i18n.locale.value = lang;
    axios.defaults.headers.common['Accept-Language'] = lang;
    document.querySelector('html').setAttribute('lang', lang);
  }

  public isLanguageSupported(lang: string) {
    return Boolean(this.languages[lang]);
  }

  public getLocalStoreLanguage(): string | null {
    return localStorage.getItem('currentLanguage');
  }
}
