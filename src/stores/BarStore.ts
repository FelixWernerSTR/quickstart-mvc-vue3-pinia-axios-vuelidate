import {defineStore} from "pinia";
import BarService from "@/services/Bar.service";
import {type IBar, Bar} from "@/model/Bar.model";


//gutes Beispiel wie man Pinia mit Typescript verwendet:
//https://runthatline.com/pinia-typescript-type-state-actions-getters/
type State = {
  bar: IBar;
  isRequestLoading: boolean;
};
const barService = new BarService();

export const useBar = defineStore("Bar", {
  state: (): State => ({
    bar: new Bar(),
    isBarValid: true,
  }),

  getters: {
    requestLoading() {
      return this.isRequestLoading;
    },
  },

  actions: {
    getBarforId(id: number): Promise<void> {
      console.debug("retrieveBar by pinia action!");
      barService
        .find(id)
        .then((res) => {
          console.debug("retrieveBar res:", res.id);
          this.bar = res;
        })
        .catch((error) => {
          //alertService().showHttpError(this, error.response);
          console.debug("retrieveBar fehlgeschlagen!", error);
        });
    },
    
    setIsBarValid(valid:boolean): Promise<boolean> {
      this.isBarValid = valid;
      return this.isBarValid;
    },
    
    setIsRequestLoading(loading:boolean): Promise<boolean> {
      this.isRequestLoading = loading;
      return this.isRequestLoading;
    },
    
    getIsRequestLoading() : Promise<boolean>{
      return this.isRequestLoading;
    },

    async saveBar(): Promise<void> {
      await this.setIsRequestLoading(true);
      console.debug("saveBar by pinia action!",  this.bar);
      console.debug("saveBar this.isRequestLoading: ", this.isRequestLoading);
            await barService
            .save(this.bar)
            .then((res) => {
              console.debug("saveBar res:", res.id);
              this.bar = res;
            })
            .catch((error) => {
              //alertService().showHttpError(this, error.response);
              console.debug("saveBar fehlgeschlagen!", error);
            });
        await this.setIsRequestLoading(false);
    },
    async save() {
      await this.getIsRequestLoading();
      if(this.isRequestLoading===true){
        alert(
          "warten Sie ein Agugenblick! Der Server antwortet noch!"
        );
      }else{
      await this.saveBar();
      }
    },
  },
});
