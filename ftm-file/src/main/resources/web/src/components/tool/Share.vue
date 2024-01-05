<template>
    <div v-if="share.show" id="share">
           <h2 >{{share.data.fname}}</h2>
            <div style="text-align: left;font-size: 20px" v-if="share.data.isShare==1">
                 分享地址：<br>
              <p><a target="_blank" :href="'http://pan.anets.top/share/'+share.data.shareAddress">http://pan.anets.top/share/{{share.data.shareAddress}}</a></p>
                 分享密码: <br>
               <p>{{share.data.sharePassword}}</p>
            </div>
      <div style="text-align: left;font-size: 20px" v-if="share.data.isShare==0">
        <p v-if="!shareTemp.show">该文件还尚未分享！</p> <p v-if="shareTemp.show">分享成功！</p>
        <div style="text-align: left;font-size: 20px" v-if="shareTemp.show">
          分享地址：<br>
          <p><a target="_blank" :href="'http://pan.anets.top/share/'+shareTemp.address">http://pan.anets.top/share/{{shareTemp.address}}</a> </p>
          分享密码: <br>
          <p>{{shareTemp.password}}</p>
        </div>
        <button type="button" v-if="!forbidden" class="layui-btn" @click="getSharez(share.data.fid)">生成分享链接</button>
        <button type="button" v-if="forbidden" class="layui-btn layui-btn-disabled">生成分享链接</button>
      </div>
         <i class="icon-guanbi iconfont" style="position:absolute;font-size:20px;right:-20px;top:-20px" @click="close()"></i>
        <!--      <blockquote class="layui-elem-quote">这个貌似不用多介绍，因为你已经在太多的地方都看到</blockquote>-->
    </div>
</template>

<script>
  import  {getShare} from "../../api/file";

  export default {
        name: "Share",
        props:["share"],
         data(){
            return {
               share:this.$props.share,
               shareTemp:{
                 show:false,
                 address:null,
                 password:null
               },
               forbidden:false
            }
         },
    computed:{
      user: {
        get() {
          return this.$store.state.user;
        }
      }
    },
        methods:{
          async getSharez(fid){
            this.forbidden=true;
            let {data:a} =await getShare(this.user.uid,fid);
            this.shareTemp.address=a.data.address;
            this.shareTemp.password=a.data.password;
            this.shareTemp.show=true;
            this.$emit("data");
            this.forbidden=false;
          },
          close(){
            this.shareTemp.show=false;
            this.$emit("close");
          }
        }
    }
</script>

<style scoped>
#share{
  position: fixed;
  z-index: 999;
  width: 500px;
  padding: 0px 20px 10px 20px;

  top:50%;
  left:50%;
  transform: translate(-50%,-50%);
  background-color: white;
  border-radius: 5px;
  box-shadow: 0px 1px 5px rgba(0,0,0,0.4);
}
  h2{
    text-align: left;
  }


  p{
    background-color: #F2F2F2;
    border-left: 5px solid #009688;
    margin-top: 5px;
    font-size: 18px;
    padding: 5px 10px;
  }
</style>
