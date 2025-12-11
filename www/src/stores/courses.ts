import { defineStore } from 'pinia'
import axios from 'axios'

interface Course {
    id: number
    title: string
    description: string
    price: number
    duration: string
    level: string
    studentsCount: number
    isPublished?: boolean
}

interface CourseCreateData {
    title: string
    description: string
    price: number
    duration: string
    level: string
}

interface CoursesState {
    courses: Course[]
    loading: boolean
}

export const useCoursesStore = defineStore('courses', {
    state: (): CoursesState => ({
        courses: [],
        loading: false
    }),

    actions: {
        async fetchCourses(): Promise<void> {
            this.loading = true
            try {
                const response = await axios.get<Course[]>('/api/courses')
                this.courses = response.data
            } catch (error) {
                console.error('Ошибка загрузки:', error)
                throw error
            } finally {
                this.loading = false
            }
        },

        async createCourse(courseData: CourseCreateData): Promise<Course> {
            try {
                const response = await axios.post<Course>('/api/courses', courseData)
                const newCourse = response.data
                this.courses.push(newCourse)
                return newCourse
            } catch (error) {
                console.error('Ошибка создания курса:', error)
                throw error
            }
        }
    }
})