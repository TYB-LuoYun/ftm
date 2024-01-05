<template>
    <div>
       <div id="nav" class=" layui-anim-fadein layui-anim-upbit col-sm-12">
         <div class="logo">
           私人云
         </div>
         <ul class="layui-nav  content" lay-filter="" style="background-color: white;" >
           <li class="layui-nav-item"><a href="/" style="color: black">网盘</a></li>
          <!-- <li class="layui-nav-item layui-this"><a href="http://anets.top">导航首页/a></li> -->
<!--           <li class="layui-nav-item"><a href="">大数据</a></li>-->
<!--           <li class="layui-nav-item">-->
<!--             <a href="javascript:;">解决方案</a>-->
<!--             <dl class="layui-nav-child"> &lt;!&ndash; 二级菜单 &ndash;&gt;-->
<!--               <dd><a href="">移动模块</a></dd>-->
<!--               <dd><a href="">后台模版</a></dd>-->
<!--               <dd><a href="">电商平台</a></dd>-->
<!--             </dl>-->
<!--           </li>-->
           <!-- <li class="layui-nav-item "><a href="http://shop.anets.top" style="color: black">商城</a></li> -->
           <!-- <li class="layui-nav-item"><a href="https://tyb-luoyun.github.io/" style="color: black">导航</a></li> -->
         </ul>


          <div class="user" style="float:right;position: absolute;right:0px;top:0px;z-index: 999;color:black">
        <p v-if="user==false" style="line-height: 60px;padding-right:10px;"><a :href="this.$store.state.passportBasicUrl+'/user/login?from='+this.$store.state.panBasicUrl">登录</a> | <a :href="this.$store.state.passportBasicUrl+'/user/register?from='+this.$store.state.panBasicUrl">注册</a></p>
        <div class="head" v-if="user&&user.uid" style="position:relative">
              <i>  <router-link to="/user/info"> {{user.username|fname}}</router-link>
              <ul style="position:absolute;left:0px;top:50px;">
                 <li @click="logout()" style="color:white">登出</li>
               </ul>
             </i>
            <router-link to="/user/info"> <span>{{user.username}}</span></router-link>
            
        </div>
      </div>



       </div>

        
    </div>
</template>

<script>
    export default {
        name: "Navigation",
      data(){
        return {

        }
      },
      filters:{
        fname(val){
           return val.substring(0,1).toUpperCase();
        }
      },
      computed:{
        user:{
          get(){
            return this.$store.state.user;
          }
        }
      },
      created(){
          //一加载就调用
        this.initLayui();
      },
      mounted(){
        //节点渲染完毕后调用
        this.initLayui();
      },methods:{
        initLayui(){
          //注意：导航 依赖 element 模块，否则无法进行功能性操作
          layui.use('element', function(){
            var element = layui.element;

          });
        },
        logout(){
           //删除本地存储的cookie
           localStorage.removeItem("token");
           //user置为false
           this.$store.commit('setUser',false);
           //请求云端删除cookie和user信息
           $.ajax({
             url:"http://localhost:8086/user/logout.action",
             type:"get",
             data:null,
             dataType:"jsonp",
             success:(data)=>{
                 if(data.success){
                    this.$layer.msg(data.msg);
                 }else{
                     this.$layer.msg(data.msg);
                 }
             },
             error:(res)=>{
                  this.$layer.msg("登出错误");
                  console.log(res);
             }
           });
         }
      }
    }
</script>

<style scoped>
a:hover{
  text-decoration: none;
}
#nav{
  text-align: left;
  display: flex;
  flex-direction: row;
  margin: 0px;
  padding: 0px;
}
#nav .logo{
  width:110px;
  height: 60px;
  color: #009E94;
  font-weight: bolder;
  font-size: 20px;
  text-align: left;
  padding-top: 13px;
  padding-left: 20px;
  border-left: 5px solid #009E94;
  background-color: white;
  cursor: default;
}
#nav .content{
  width: 100%;
  border-radius: 0px;
}

#nav .content ul li{
  /*color: #009E94 !important;*/
  background-color: deeppink !important;
}



 .user a{
    color:black;
  }
.user a:hover{
  color:#ee1289;

}
.user{
  /* background-color: #ee1289; */
  height: 60px;
  line-height: 60px;
}
.user i{
  text-align: center;
  justify-content: center;
  width:40px;
  height:40px;
  line-height:40px;
  background-color: #ee1289;
  display: inline-block;
  border-radius: 50%;
  margin-top:10px;
  font-size: 20px;
  font-style: normal;
  
  
}

.user i:hover{
   /* background-color: white; */
}

.user i:hover a{
   color: white;
}

.user span{
  margin-left:10px;
  margin-right:20px;
  margin-top:0px;
   height: 60px;
  line-height: 60px;
  
}
.user .head{
  display:flex;
  flex-direction: row;
  height: 60px;
  line-height: 60px;
  /* background-color: #ee1289; */
}
.user .head i:hover ul{
   display: block;
}

.user .head ul{
  display: flex;
  flex-direction: column;
  padding: 10px 0px;
  background-color: rgba(0,0,0,0.8);
  display: none;
  border-radius: 5px;
  font-size: 14px;
  cursor: default;
}

.user .head ul li{
  
  height: 20px;
  width: 100px;
  line-height: 10px;
  padding: 5px 10px;
}

.user .head ul li:hover{
  background-color: rgba(255,255,255,0.1);
}
</style>
