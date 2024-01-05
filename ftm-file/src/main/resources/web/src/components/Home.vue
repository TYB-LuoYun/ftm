<template>
    <div id="all"  class="col-md-12" style="">
        <Navigation></Navigation>
         <div class="content col-sm-12" v-if="user" >
           <Files></Files>
           <User></User>
         </div>

         <div class="mask" v-if="!user">
             <img src="/static/image/cat.gif" height="150" alt=""> <div style="margin-left:50px">
               <h2>Anets.top | 轻量级私人云盘</h2>
               <br>
                你没有权限进入！<a :href="this.$store.state.passportBasicUrl+'/user/login?from='+this.$store.state.panBasicUrl">请登陆！</a>
             </div>
         </div>
    </div>
</template>

<script>
  import Navigation from '../components/Home/Navigation'
  import Files from '../components/Home/Files'
  import User from '../components/Home/User'
    export default {
        name: "Home",
      components:{Navigation,Files,User},
      data(){
        return {}
      },
      computed:{
        user:{
          get(){
            return this.$store.state.user;
          }
        }
      },
      created(){
        this.initUser()
      },
      methods:{
         initUser(){
            //从本地取user。如果能，那么不用从服务端获取用户，只需要通过token获取session
            //  if( this.$cookie.get("token")||localStorage.getItem("token")){
            //    console.log("token",this.$cookie.get("token"));
            //    //通过token获取用户信息
            //    this.gainUserByToken(this.$cookie.get("token")||localStorage.getItem("token"));
            //  }else{
            //    this.$store.commit("setUser",false)
            //    console.log("cookie不存在");
            //    //需要登陆
            //   //  alert("需要登陆")
            //  }
          },
        gainUserByToken(token){
            $.ajax({
              url:this.$store.state.passportServiceUrl+'/user/token/'+token+'.action',
              type:'GET',
              dataType:'jsonp',
              success:(data)=>{
                console.log(data);
                if(data.success){
                  this.$store.commit("setUser",data.data)
                  console.log(this.$store.state.user);
                  localStorage.setItem("token",token);
                }else{
                  console.log(data.msg);
                  //  this.$layer.msg(data.msg);
                   this.$store.commit("setUser",false)
                  localStorage.removeItem("token");
                }
              },
              error:(data)=>{
                console.log("失败",data);
                 this.$store.commit("setUser",false)
                 localStorage.removeItem("token");
              }
            });
        }
      }
    }
</script>

<style scoped>
.content{

  /*display: flex;*/
  /*flex-direction: row;*/
  padding: 0px;
  /*background-color: red;*/
  /*margin-left: -10px;*/
  position: relative;


}

  #all{
    padding: 0px;
  }


.mask{
  padding:50px 100px;
  position: fixed;
  top:50%;
  left:50%;
  background-color: white;
  transform: translate(-50%,-50%);
  border-radius: 10px;
  display: flex;
  flex-direction: row;
  text-align: center;
}
</style>
