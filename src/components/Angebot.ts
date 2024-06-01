import { defineComponent, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useAngebot } from "@/stores/AngebotStore";
import { useVuelidate } from '@vuelidate/core'
import { maxLength } from '@vuelidate/validators'
import { Angebot } from "@/model/Angebot.model";
import { useI18n } from 'vue-i18n';
import { useValidation } from '@/config/validation';

export default defineComponent({
  name: "Angebot",
  components: {
  },
  setup() { // do not use the keyword 'this' in the setup method -> 'this' is undefined in the setup method
    const angebotStore = useAngebot();
    const { angebot, isRequestLoading, isAngebotValid } = storeToRefs(angebotStore);

    function save() {
      console.debug("angebot:", angebot.value);
      angebotStore.save();
    }
    
    function changeState() {
      console.debug("changeState");
      angebotStore.changeState();
    }
    
    const inputAngebotId = ref(1);
    
    function getAngebotForId() {
      console.log("getAngebotForId:", inputAngebotId.value);
      angebotStore.getAngebotforId(inputAngebotId.value);
    }

    //https://runthatline.com/pinia-watch-state-getters-inside-vue-components/
    watch(angebot.value, () => {
      console.debug('Angebot ref changed, do something!')
    })

    
    //https://vuelidate-next.netlify.app/
    //https://lokalise.com/blog/vue-i18n/
    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
        mustBeCool: validations.mustBeCool(t$('entity.validation.mustBeCool').toString()),
        customContains: validations.customContains(t$('entity.validation.customContains', { text: 'cool' }).toString(), 'cool'),
        //pattern: validations.pattern(t$('entity.validation.pattern', { pattern: '^[a-zA-Z]*' }).toString(), '^[a-zA-Z]*'),
      },
      // versicherungsbeginn: {
      //   between: validations.between(t$('entity.validation.between', { min: HelperService.getDateFormated(new Date(2023,9,9)), max: HelperService.getDateFormated(new Date(2025,11,9)) }).toString(), HelperService.getDateFormated(new Date(2023,9,9)), HelperService.getDateFormated(new Date(2025,11,9))),
      // }
    }

    let v$ = useVuelidate(validationRules, angebot.value)
    v$.value.$validate();

    watch(v$, () => {
      console.debug('v$ changed, do something!', v$)
      if(v$.value.$errors.length>0){
        console.debug('v$.$errors', v$.value.$errors);
         angebotStore.setIsAngebotValid(false);
      }else{
         angebotStore.setIsAngebotValid(true);
      }
    });

    return {
      angebot,
      isAngebotValid,
      save,
      changeState,
      getAngebotForId,
      inputAngebotId,
      v$,
      t$,
    };
  },
});
