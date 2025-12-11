<script lang="js">
export default {
  mounted() {
    let aside = document.querySelector("aside");
    let menus = document.querySelectorAll(".btn-description");
    aside.addEventListener("mouseenter", (e) => {
      aside.classList.add("active");
    })

    aside.addEventListener("mouseleave", (e) => {
      aside.classList.remove("active");
    })

  }
}

</script>

<template>
  <aside>
    <nav>
      <ul>
        <li class="btn section-type"><img src="../assets/main-page.png" alt="" width="20px">
          <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;Главная</span>
        </li>
        <li class="btn section-type"><img src="../assets/folder.png" alt="" width="20px">
          <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;Мои курсы</span>
        </li>
        <li class="btn section-type"><img src="../assets/catalog.png" alt="" width="20px">
          <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;Каталог курсов</span>
        </li>
        <li class="btn section-type"><img src="../assets/sale.png" alt="" width="20px">
          <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;Акции</span>
        </li>
        <li class="btn section-type"><img src="../assets/profile.png" alt="" width="20px">
          <span class="btn-description">&nbsp;&nbsp;&nbsp;&nbsp;Профиль</span>
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
}

li {
  display: flex;
  align-items: center;
}

.btn-description {
  width: 0;
  opacity: 100%;
  overflow-x: hidden;
  text-wrap: nowrap;
  transition: width 0.5s;
}

.active nav ul li .btn-description {
  width: 100%!important;
  opacity: 100%;
}

ul {
  width: 20px;
  transition: width 0.5s;
}

.active nav ul {
  width: 160px!important;
}

</style>