import { createRouter, createWebHistory } from "vue-router";
import { createPinia, storeToRefs } from "pinia";
import { useBar } from "@/stores/BarStore";
import { Bar } from "../model/Bar.model";

      
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "bar",
      component: () => import("../components/Bar.vue"),
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
  ],
});

export default router;
