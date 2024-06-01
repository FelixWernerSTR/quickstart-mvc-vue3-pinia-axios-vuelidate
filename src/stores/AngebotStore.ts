import { defineStore } from "pinia";
import AngebotService from "@/services/Angebot.service";
import { Angebot } from "@/model/Angebot.model";

//gutes Beispiel wie man Pinia mit Typescript verwendet:
//https://runthatline.com/pinia-typescript-type-state-actions-getters/
type State = {
  angebot: Angebot;
  isRequestLoading: boolean;
};
const angebotService = new AngebotService();

export const useAngebot = defineStore("Angebot", {
  state: (): State => ({
    angebot: new Angebot(null,"leer",null,null,null,null),
    isAngebotValid: false,
  }),

  getters: {
    requestLoading() {
      return this.isRequestLoading;
    },
  },

  actions: {
    getAngebotforId(id: number): Promise<void> {
      console.debug("retrieveAngebot by pinia action!");
      angebotService.find(id).then((res) => {
          console.debug("retrieveAngebot res:", res.id);
          this.angebot.id = res.id;
          this.angebot.name = res.name;
          this.angebot.angebotsnummer = res.angebotsnummer;
          this.angebot.schlagwort = res.schlagwort;
          this.angebot.versicherungsbeginn = res.versicherungsbeginn;
          this.angebot.buJN = res.buJN; 
        }).catch((error) => {
          //alertService().showHttpError(this, error.response);
          console.debug("retrieveAngebot fehlgeschlagen!", error);
        });
    },
    
    setIsAngebotValid(valid:boolean): Promise<boolean> {
      this.isAngebotValid = valid;
      return this.isAngebotValid;
    },
    
    setIsRequestLoading(loading:boolean): Promise<boolean> {
      this.isRequestLoading = loading;
      return this.isRequestLoading;
    },
    
    getIsRequestLoading() : Promise<boolean>{
      return this.isRequestLoading;
    },

    async save() {
      await this.getIsRequestLoading();
      if(this.isRequestLoading===true){
        alert(
          "warten Sie ein Agugenblick! Der Server antwortet noch!"
        );
      }else{
      await this.callRESTSaveAngebot();
      }
    },
    async callRESTSaveAngebot(): Promise<void> {
        await this.setIsRequestLoading(true);
        console.debug("callRESTSaveAngebot this.isRequestLoading: ", this.isRequestLoading);
        await angebotService.save(this.angebot).then((res) => {
          console.debug("callRESTSaveAngebot res:", res.id);
          this.angebot.id = res.id;
          this.angebot.name = res.name;
          this.angebot.angebotsnummer = res.angebotsnummer;
          this.angebot.schlagwort = res.schlagwort;
          this.angebot.versicherungsbeginn = res.versicherungsbeginn;
          this.angebot.buJN = res.buJN; 
        }).catch((error) => {
          //alertService().showHttpError(this, error.response);
          console.debug("callRESTSaveAngebot fehlgeschlagen!", error);
        });
        await this.setIsRequestLoading(false);
    },
    async changeState(): Promise<void> {
      this.angebot.name="changed state reactively made visible in input field!";
      console.debug("changeState by pinia action!",  this.angebot);
    },
  },
});
