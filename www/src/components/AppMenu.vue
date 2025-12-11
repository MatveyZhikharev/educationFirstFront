<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'

const isActive = ref(false)
const menuItems = [
  { to: '/', label: 'Главная', icon: new URL('../assets/main-page.png', import.meta.url).href },
  { to: '/my-courses', label: 'Мои курсы', icon: new URL('../assets/folder.png', import.meta.url).href },
  { to: '/courses', label: 'Каталог курсов', icon: new URL('../assets/catalog.png', import.meta.url).href },
  { to: '/sales', label: 'Акции', icon: new URL('../assets/sale.png', import.meta.url).href },
  { to: '/profile', label: 'Профиль', icon: new URL('../assets/profile.png', import.meta.url).href },
  { to: '/admin', label: 'Админ', icon: new URL('../assets/profile.png', import.meta.url).href },
]
</script>

<template>
  <aside :class="{ active: isActive }" @mouseenter="isActive = true" @mouseleave="isActive = false">
    <nav>
      <ul>
        <li v-for="item in menuItems" :key="item.to" class="btn section-type">
          <RouterLink :to="item.to" class="menu-link">
            <img :src="item.icon" alt="" width="20px">
            <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;{{ item.label }}</span>
          </RouterLink>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<style scoped>
aside {
  position: fixed;
  margin-top: 10px;
  background-color: #ffffff;
  box-shadow: 10px 0px 25px #f1f1f1;
}

ul {
  padding: 0 15px;
  display: flex;
  gap: 30px;
  flex-direction: column;
  list-style-type: none;
  width: 20px;
  transition: width 0.5s;
}

li {
  display: flex;
  align-items: center;
}

.menu-link {
  display: flex;
  align-items: center;
  color: inherit;
  text-decoration: none;
}

.btn-description {
  width: 0;
  opacity: 100%;
  overflow-x: hidden;
  text-wrap: nowrap;
  transition: width 0.5s;
}

.active nav ul li .btn-description {
  width: 100%;
  opacity: 100%;
}

.active nav ul {
  width: 170px;
}
</style>
