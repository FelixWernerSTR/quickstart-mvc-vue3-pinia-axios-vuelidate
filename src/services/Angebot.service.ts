import axios from "axios";

import { Angebot } from "@/model/Angebot.model";

const baseApiUrl = __VITE_BASE_API_URL_ANGEBOT__;

export default class AngebotService {

  public find(id: number): Promise<Angebot> {
    return new Promise<Angebot>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then((res: any) => {
          resolve(res.data);
        })
        .catch((err: any) => {
          reject(err);
        });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl)
        .then((res: any) => {
          resolve(res);
        })
        .catch((err: any) => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then((res: any) => {
          resolve(res);
        })
        .catch((err: any) => {
          reject(err);
        });
    });
  }

  public save(entity: Angebot): Promise<Angebot> {
    return new Promise<Angebot>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then((res: any) => {
          resolve(res.data);
        })
        .catch((err: any) => {
          reject(err);
        });
    });
  }

}
