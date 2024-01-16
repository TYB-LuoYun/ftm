import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
  mode:'hash',
  routes: [
    {
      path: '/',
      component: ()=>import('@/components/Home')
    },{
      path: '/All/*',
      component: ()=>import('@/components/Home')
    },
    {
      path: '/All',
      component: ()=>import('@/components/Home')
    },
    {
      path:'/share/:key',
      component: ()=>import('@/components/Access')
    },
    {
      path:'/share/:key/*',
      component: ()=>import('@/components/Access')
    }
  ]
})
