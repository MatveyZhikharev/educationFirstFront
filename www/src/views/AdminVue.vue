<script lang="ts">
export default {
  data() {
    return {
      course: {
        title: "Основы ремонта техники",
        description: "Научитесь ремонтировать технику, используя отвертку, руки и силу мыслей",
        duration: "24 часа",
        level: "Начальный",
        studentsCount: 1245
      },
      materials: [
        {
          title: "Знакомство",
          description: "Вводный урок по ремонту техники",
          duration: "48:52"
        },
        {
          title: "Устройство смартфона",
          description: "Изучаем внутреннее устройство",
          duration: "1:30:00"
        },
        {
          title: "Работа с инструментами",
          description: "Основные инструменты ремонтника",
          duration: "45:15"
        },
        {
          title: "Диагностика неисправностей",
          description: "Как найти проблему",
          duration: "1:15:30"
        }
      ],
      newMaterial: {
        title: "",
        description: "",
        duration: ""
      },
      isEditing: false
    }
  },
  methods: {
    addMaterial() {
      if (this.newMaterial.title) {
        this.materials.push({ ...this.newMaterial })
        this.newMaterial = { title: "", description: "", duration: "" }
      }
    },
    removeMaterial(index: number) {
      this.materials.splice(index, 1)
    },
    saveCourse() {
      this.isEditing = false
      // Здесь будет логика сохранения
      console.log("Курс сохранен:", this.course)
    },
    editCourse() {
      this.isEditing = true
    }
  }
}
</script>

<template>
  <div class="course-container">
    <div class="course-header">
      <div v-if="!isEditing">
        <h1 class="course-title">{{ course.title }}</h1>
        <p class="course-description">{{ course.description }}</p>
        <button @click="editCourse" class="edit-btn">Редактировать курс</button>
      </div>
      <div v-else>
        <input v-model="course.title" class="course-title-input">
        <textarea v-model="course.description" class="course-description-input"></textarea>
      </div>

      <div class="course-meta">
        <div class="meta-tags">
          <div class="meta-tag">
            <span v-if="!isEditing">{{ course.duration }}</span>
            <input v-else v-model="course.duration" class="meta-input">
          </div>
          <div class="meta-tag">
            <span v-if="!isEditing">{{ course.level }}</span>
            <select v-else v-model="course.level" class="meta-select">
              <option>Начальный</option>
              <option>Средний</option>
              <option>Продвинутый</option>
            </select>
          </div>
          <div class="meta-tag">
            <span v-if="!isEditing">{{ course.studentsCount }} студентов</span>
            <input v-else v-model="course.studentsCount" type="number" class="meta-input">
          </div>
        </div>

        <div class="instructor-info">
          <img
              src="https://sun1-85.userapi.com/s/v1/ig2/JquCXZGJElIMQC952QDJEeZfTBDgu5OdiROVOHKdguWo_-LFtxR09nGwdXhMecxOvSH2y6bTIsxvDyi8NR8PGDF1.jpg?quality=95&crop=392,386,328,328&as=32x32,48x48,72x72,108x108,160x160,240x240&ava=1&u=Kx6FRuRqcgp0kBMoK6rQAqva5ZjWDneWjG7NIeBuqns&cs=400x400"
              alt="Преподаватель" class="instructor-avatar">
          <div class="instructor-details">
            <h4 class="instructor-name">Вершинин Максим</h4>
            <p class="instructor-description">Старший разработчик, гений и миллиардер.</p>
          </div>
        </div>
      </div>
    </div>

    <div class="course-content">
      <section class="content-section">
        <h2 class="section-title">Описание</h2>
        <p v-if="!isEditing">{{ course.description }}</p>
        <textarea v-else v-model="course.description" class="description-textarea"></textarea>
      </section>

      <section class="content-section">
        <div class="section-header">
          <h2 class="section-title">Материалы курса</h2>
          <button v-if="isEditing" @click="saveCourse" class="save-btn">Сохранить</button>
        </div>

        <!-- Форма добавления нового материала -->
        <div v-if="isEditing" class="add-material-form">
          <h3>Добавить материал</h3>
          <div class="form-row">
            <input v-model="newMaterial.title" placeholder="Название урока" class="material-input">
            <input v-model="newMaterial.duration" placeholder="Длительность (например: 45:00)" class="material-input">
          </div>
          <textarea v-model="newMaterial.description" placeholder="Описание урока" class="material-textarea"></textarea>
          <button @click="addMaterial" class="add-btn">Добавить материал</button>
        </div>

        <div class="material-list">
          <div class="material-item" v-for="(material, index) in materials" :key="index">
            <div class="item-preview">
              <span class="item-duration">{{ material.duration }}</span>
              <button v-if="isEditing" @click="removeMaterial(index)" class="remove-btn">×</button>
            </div>
            <div v-if="!isEditing">
              <h5 class="item-header">{{ material.title }}</h5>
              <p class="item-description">{{ material.description }}</p>
            </div>
            <div v-else class="edit-material">
              <input v-model="material.title" class="material-title-input">
              <textarea v-model="material.description" class="material-desc-input"></textarea>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.course-container {
  padding: 0 100px;
  width: 100%;
}

.meta-tag {
  display: inline-block;
  border: #f1f1f1 solid 1px;
  border-radius: 15px;
  padding: 5px 20px;
  height: 20px;
  margin-right: 10px;
}

.content-section {
  margin: 10px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 0.5px solid #e0e0e0;
  user-select: none;
}

.toggle-icon {
  font-size: 18px;
  transition: transform 0.3s ease;
}

.material-list {
  display: grid;
  margin: 20px 0;
  grid-template-columns: repeat(auto-fill, 300px);
  gap: 20px;
  justify-content: center;
}

.material-item {
  height: 270px;
  border: 1px solid #e0e0e0;
  border-radius: 15px;
  overflow: hidden;
  position: relative;
}

.item-preview {
  position: relative;
  background-image: url(https://avatars.mds.yandex.net/i?id=57b6f6ee1e1c40386ef66bc8a18f6b19e079f0ef-4103093-images-thumbs&n=13);
  height: 200px;
  width: 300px;
  background-size: cover;
  border-bottom: 1px solid #e0e0e0;
}

.item-duration {
  position: absolute;
  right: 10px;
  bottom: 10px;
  background: rgba(0,0,0,0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.item-header {
  padding: 0 10px;
  margin: 10px 0;
}

.item-description {
  padding: 0 10px;
  margin: 0;
  font-size: 14px;
  color: #666;
}

.course-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.instructor-info {
  display: flex;
  align-items: center;
  gap: 25px;
}

.instructor-name {
  margin: 5px 0;
}

.instructor-description {
  margin: 0;
}

.instructor-avatar {
  width: 50px;
  height: 50px;
  border-radius: 25px;
}

/* Стили для админ-панели */
.edit-btn, .save-btn, .add-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  margin: 10px 0;
}

.save-btn {
  background: #28a745;
}

.add-btn {
  background: #17a2b8;
  width: 100%;
  margin-top: 10px;
}

.remove-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #dc3545;
  color: white;
  border: none;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.course-title-input, .course-description-input {
  width: 100%;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 10px;
  margin: 5px 0;
  font-size: 16px;
}

.course-title-input {
  font-size: 24px;
  font-weight: bold;
}

.course-description-input {
  font-size: 14px;
  resize: vertical;
}

.meta-input, .meta-select {
  border: 1px solid #e0e0e0;
  border-radius: 15px;
  padding: 5px 10px;
  width: 100%;
}

.description-textarea {
  width: 100%;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  resize: vertical;
  min-height: 100px;
}

.add-material-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 10px;
  margin: 20px 0;
}

.form-row {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.material-input, .material-textarea {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 10px;
  flex: 1;
}

.material-textarea {
  width: 100%;
  resize: vertical;
  min-height: 60px;
}

.edit-material {
  padding: 10px;
}

.material-title-input, .material-desc-input {
  width: 100%;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 8px;
  margin-bottom: 5px;
  font-size: 14px;
}

.material-desc-input {
  resize: vertical;
  min-height: 40px;
  font-size: 12px;
}
</style>