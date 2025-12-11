import { createRouter, createWebHistory } from 'vue-router'
import AboutView from "@/views/AboutView.vue"
import AdminView from "@/views/AdminVue.vue"
import ComingSoonView from "@/views/ComingSoonView.vue"
import HomeView from '@/views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView,
    },
    {
      path: '/my-courses',
      name: 'my-courses',
      component: ComingSoonView,
      props: { title: 'Мои курсы' },
    },
    {
      path: '/courses',
      name: 'courses',
      component: ComingSoonView,
      props: { title: 'Каталог курсов' },
    },
    {
      path: '/sales',
      name: 'sales',
      component: ComingSoonView,
      props: { title: 'Акции' },
    },
    {
      path: '/profile',
      name: 'profile',
      component: ComingSoonView,
      props: { title: 'Профиль' },
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
    },
  ],
})

export default router
