import axios from "axios";

import { IBar } from "@/model/Bar.model";

const baseApiUrl = __VITE_BASE_API_URL_BAR__;

export default class BarService {

  public find(id: number): Promise<IBar> {
    return new Promise<IBar>((resolve, reject) => {
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

  public save(entity: IBar): Promise<IBar> {
    return new Promise<IBar>((resolve, reject) => {
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
