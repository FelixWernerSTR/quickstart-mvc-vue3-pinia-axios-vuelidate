import {defineComponent, ref, watch} from "vue";
import { useRouter} from "vue-router";
import {storeToRefs} from "pinia";
import {useBar} from "@/stores/BarStore";
import {useVuelidate} from '@vuelidate/core'
import {maxLength} from '@vuelidate/validators'
import { IBar } from "@/model/Bar.model";
import { useI18n } from 'vue-i18n';
import { useValidation } from '@/config/validation';

export default defineComponent({
  name: "Bar",
  components: {
  },
  setup() { // do not use the keyword 'this' in the setup method -> 'this' is undefined in the setup method
    const barStore = useBar();
    const { bar, isRequestLoading } = storeToRefs(barStore);
    
    function save() {
      console.debug("bar:", bar.value);
      barStore.save();
    }
    
    function getBarForId() {
      console.log("getBarForId:", bar.id);
      //TODO
    }

    //https://runthatline.com/pinia-watch-state-getters-inside-vue-components/
    watch(bar, () => {
      console.debug('Bar ref changed, do something!')
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

    let v$ = useVuelidate(validationRules, bar.value)
    v$.value.$validate();

    watch(v$, () => {
      console.debug('v$ changed, do something!', v$)
      if(v$.value.$errors.length>0){
        console.debug('v$.$errors', v$.value.$errors);
         barStore.setIsBarValid(false);
      }else{
         barStore.setIsBarValid(true);
      }
    });

    return {
      bar,
      save,
      getBarForId,
      v$,
      t$,
    };
  },
});
