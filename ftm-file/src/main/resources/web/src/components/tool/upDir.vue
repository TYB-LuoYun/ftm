<template>
  <div id="upFiles1" v-if="show">
    <h4 v-if="file.done!=file.length">文件上传中 | 上传进度（{{file.done}}/{{file.length}}）</h4>
    <h4 v-if="file.done==file.length" style="position: relative">上传完成<i class="layui-icon layui-icon-face-smile-b" style="position:absolute;left:90px;bottom:-1px;font-size: 20px; color:#009E94;"></i>
      <i @click="closeDialog()" class="iconfont icon-guanbi" style="color:#009E94;position:absolute;right:10px;"></i>
    </h4>
    <table class="layui-table" lay-skin="nob" style="text-align:left">
      <colgroup>
        <!--          <col width="150">-->
        <!--          <col width="200">-->
      </colgroup>

      <tbody>
      <tr v-for="(item,index) in files" :key="index">
        <td class="flag">
          <i class="iconfont icon-tupian" style="color:#009E94;" v-show="types[index]=='image'"></i>
          <i class="iconfont icon-dc-icon-zhongzidujiaoshou" style="color:#009E94;" v-show="types[index]=='zhongzi'"></i>
          <i class="iconfont icon-wendang" style="color:#009E94;" v-show="types[index]=='document'"></i>
          <i class="iconfont icon-shipin" style="color:#009E94;" v-show="types[index]=='video'"></i>
          <i class="iconfont icon-yasuobao" style="color:#009E94;" v-show="types[index]=='package'"></i>
          <i class="iconfont icon-yinle2" style="color:#009E94;" v-show="types[index]=='music'"></i>
          <i class="iconfont icon-yingyong" style="color:#009E94;" v-show="types[index]=='other'"></i>
          <i class="iconfont icon-qitatongyong" style="color:#009E94;" v-show="types[index]=='application'"></i>
        </td>
        <td>{{item.webkitRelativePath}}</td>
        <td>{{item.name}}</td>
        <td>{{item.size|forSize}}</td>
        <td style="width: 150px;height:10px;position: relative">
          <div class="progress" style="height: 10px;margin-bottom: -3px" v-if="success[index]==false&&index==file.index">
            <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60"
                 aria-valuemin="0" aria-valuemax="100"  :style="{width: progressz[index]}">
              <span class="sr-only">40% 完成</span>
            </div>
          </div>
          <div  v-if="success[index]==true" style="position: relative;text-align:center">
            <i class="layui-icon layui-icon-ok-circle" style="position:absolute;bottom:-12px;font-size: 20px; color:#009E94;"></i>
          </div>
          <div  v-if="success[index]!=true&&success[index]!=false" style="position: relative">
            <i class="layui-icon layui-icon-face-cry" style="position:absolute;left:0px;bottom:-12px;font-size: 20px; color:#FF5722;"><span style="font-size: 14px;">{{success[index]}}</span></i>
          </div>
          <div v-if="progressz[index]=='0%'&&index!=file.index+1&&index!=file.index" style="position: relative">
            排队中 &nbsp;&nbsp; <i class="layui-icon layui-icon-loading" style="position:absolute;bottom:-0px;font-size: 20px; color:#009E94;"></i>
          </div>
          <div v-if="progressz[index]=='0%'&&index==file.index+1" style="position: relative">
            请求中  &nbsp;&nbsp;<i class="layui-icon layui-icon-engine" style="position:absolute;bottom:-0px;font-size: 20px; color:#009E94;"></i>
          </div>
        </td>
        <td>操作（续传|暂停）</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import axios from 'axios'
  import {getSpace} from '../../api/file'
  axios.defaults.baseURL = '/api';
  export default {
    name: "upFiles",
    props:["show"],
    data(){
      return{
        file:{
          length:0,
          done:0,
          index:0
        },
        progrees:[],
        types:[],
        success:[]

      }
    },
    computed:{
      files:{
        get(){
          return this.$store.state.files;
        }
      },
      user:{
        get(){
          return this.$store.state.user;
        }
      },
      progressz:{
        get(){
          return this.progrees;
        }
      },
      parentId:{
        get(){
          return this.$store.state.parentId;
        }
      }
    },
    watch:{
      files:{
        handler(newVal,oldVal){

          if(this.$props.show){
            this.upFiles();
          }
        },
        deep:true,
        immediate:false
      }
    },
    methods:{
      async space(){

        let {data:data}=await getSpace();
        console.log(data.data);
        this.$store.commit("setSpace",data.data);
      },
      async upFiles(){
        // this.$props.show=true;
        this.file.done=0;
        this.file.length=this.files.length;
        for(var i=0;i<this.files.length;i++){
          this.progrees[i] = "0%";
          this.success[i] = false;
          var extName=this.files[i].name.substring(this.files[i].name.lastIndexOf('.')+1);
          if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            this.$set(this.types,i,"image");
          }else if("rar,7z,zip".indexOf(extName)!=-1){
            this.$set(this.types,i,"package");
          }else if("exe,apk".indexOf(extName)!=-1){
            this.$set(this.types,i,"application");
          }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            this.$set(this.types,i,"video");
          }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            this.$set(this.types,i,"music");
          }else if("doc,excel,ppt,pdf,chm,txt,md,docx,xls".indexOf(extName)!=-1){
            this.$set(this.types,i,"document");
          }else if("torrent".indexOf(extName)!=-1){
            this.$set(this.types,i,"zhongzi");
          }else{
            this.$set(this.types,i,"other");
          }
        }
        for(var i=0;i<this.files.length;i++){
          this.file.index=i;
          var form = new FormData();
          form.append('file', this.files[i]);
          form.append('webkitRelativePath',this.files[i].webkitRelativePath);
          console.log(this.files[i].webkitRelativePath);
          form.append('parentId', this.parentId);
          form.append('userId',this.user.uid);
          var config = {
            onUploadProgress: progressEvent => {
              var complete = (progressEvent.loaded / progressEvent.total * 100 | 0) + '%';
              this.$set(this.progrees,i,complete);
            }
          }
          await axios.post(`file/uploadDir.action`,
            form, config).then((res) => {
            this.$set(this.progrees,i,"100%");

            if(res.status==200&&res.data.code=="200"){
              this.$set(this.success,i,true);
              console.log("=======================================ok");
              this.file.done=i+1;
            }else{
              if(res.data.msg==""){
                this.$set(this.success,i,"服务器错误");
              }else{
                this.$set(this.success,i,res.data.msg);
              }

              // this.$set(this.msgs,i,res.data.msg);
              console.log("=======================================error");
            }

            console.log(res);
          });
        }
        console.log("完成");
        this.$emit("data");
        this.space();



      },closeDialog(){
        this.$emit("close");
      }
    },
    filters:{
      forSize(size){
        if((size/1024/1024/1024).toFixed(2)>1){
          return (size/1024/1024/1024).toFixed(2)+"GB"
        }else  if((size/1024/1024).toFixed(2)>1){
          return (size/1024/1024).toFixed(2)+"MB"
        }else if((size/1024).toFixed(2)>1){
          return (size/1024).toFixed(2)+"KB"
        }else{
          return (size).toFixed(2)+"BYTES"
        }

      }
    }
  }
</script>

<style scoped>
  #upFiles1{
    /*width:600px;*/
    max-height: 100%;
    padding-bottom: 10px;
    position: fixed;
    bottom: 0px;
    right:0px;
    background-color: white;
    z-index: 999;
    box-shadow: 2px 2px 5px black;
    border-top-left-radius: 10px;
    overflow: auto;
  }
  h4{
    text-align: left;
    border-left: 10px solid #009E94;
    background-color: #F2F2F2;
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
    margin-top: 0px;
    border-top-left-radius: 10px;
    color:#009E94;
  }
  .flag{
    position: relative;
  }
  .flag i{
    position: absolute;
    bottom: 6px;
    font-size: 20px;
  }
</style>
