import { createRouter, createWebHistory } from "vue-router";
import { createPinia, storeToRefs } from "pinia";
import { useAngebot } from "@/stores/AngebotStore";
import { Angebot } from "../model/Angebot.model";

      
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "angebot",
      component: () => import("../components/Angebot.vue"),
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
  ],
});

export default router;
