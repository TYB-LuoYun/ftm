// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import $ from 'jquery'
 import  '../node_modules/bootstrap/dist/css/bootstrap.css';
 import '../node_modules/bootstrap/dist/js/bootstrap.js';
import Cookies from 'js-cookie'
Vue.config.productionTip = false


import VueCookie from 'vue-cookie'
Vue.use(VueCookie)   // 挂在在全局了

import Vuex from 'vuex'
import logger from 'vuex/dist/logger'
Vue.use(Vuex)
const state={
  files:null,
  query:null,
  space:null,
  user:{
    uid:-1,
    username:"admin"
  },
  parentId:0,
  passportBasicUrl:'http://passport.anets.top',
  passportServiceUrl:'http://passport.anets.top:8086',
  panBasicUrl:'http://pan.anets.top'
}
//突变
const mutations={
  setFiles(state,files){
     state.files=files;
  },
  setParentId(state,parentId){
    state.parentId=parentId;
  },
  setQuery(state,query){
    state.query=query;
  },
  setSpace(state,space){
    state.space=space;
  },
  setUser(state,user){
    state.user=user;
  }
}
let store=new Vuex.Store({
  state:state,//状态盒
  strict:true,
  mutations:mutations
});
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store:store,
  components: { App },
  template: '<App/>'
})
