import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const Layout = () => import('../views/Layout.vue')
const Home = () => import('../views/Home.vue')
const Orders = () => import('../views/Orders.vue')
const OrderDetail = () => import('../views/OrderDetail.vue')
const CreateOrder = () => import('../views/CreateOrder.vue')
const Messages = () => import('../views/Messages.vue')
const Chat = () => import('../views/Chat.vue')
const Profile = () => import('../views/Profile.vue')
const ProfileEdit = () => import('../views/ProfileEdit.vue')
const Arbitration = () => import('../views/Arbitration.vue')
const About = () => import('../views/About.vue')
const SearchResult = () => import('../views/SearchResult.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'Login', component: Login },
    { path: '/register', name: 'Register', component: Register },
    {
      path: '/',
      component: Layout,
      children: [
        { path: '', redirect: '/home' },
        { path: 'home', name: 'Home', component: Home },
        { path: 'orders', name: 'Orders', component: Orders },
        { path: 'messages', name: 'Messages', component: Messages },
        { path: 'profile', name: 'Profile', component: Profile }
      ]
    },
    { path: '/orders/create', name: 'CreateOrder', component: CreateOrder },
    { path: '/orders/:id', name: 'OrderDetail', component: OrderDetail },
    { path: '/chat/:userId', name: 'Chat', component: Chat },
    { path: '/profile/edit', name: 'ProfileEdit', component: ProfileEdit },
    { path: '/arbitration', name: 'Arbitration', component: Arbitration },
    { path: '/arbitration/apply', name: 'ArbitrationApply', component: () => import('../views/ArbitrationApply.vue') },
    { path: '/about', name: 'About', component: About },
    { path: '/search', name: 'SearchResult', component: SearchResult },
    { path: '/settings', name: 'Settings', component: () => import('../views/SettingsView.vue') },
  ]
})

// 简单登录守卫：除了 login/register 其它都要 token
router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const auth = useAuthStore()
  if (authRequired && !auth.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
