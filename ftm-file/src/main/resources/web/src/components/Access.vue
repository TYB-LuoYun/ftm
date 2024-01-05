<template>
    <div id="access">
       <div v-if="info.show" class="info">
                <span style="border-radius:3px;display:block;line-height:20px;background-color: white;padding: 3px 5px;padding-left30px;position: absolute;top:50%;left:50%;padding-left:30px;transform: translate(-50%,-50%)">
                  <i class="iconfont icon-wangluo" style="font-size: 20px;position: absolute;top:7px;left:7px"></i>query...,please wait!</span>
       </div>
      <div v-if="!access.show&&!files.show&&!info.show" class="tip">
        <h2>404 | 文件找不到，可能原因</h2>
        <p style="margin-top: 15px">0不存在该文件<br>1你输入的地址有误(区分大小写) <br> 2该文件已经取消分享!</p>
      </div>
       <div class="paner" v-if="access.show">
           <span class="flag">Anets.top</span>
           <div class="head">
             <img src="https://ss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/netdisk.1.22c937a6.ocMJQn2Ar3U1AxsoaZA5yw.jpg" alt="">
             <span style="font-size: 15px;font-weight: bold">{{file.user.username}}</span>  <span style="font-size: 11px;line-height: 20px;opacity: 0.5">给你分享了加密文件</span>
             <span style="position: absolute;left:65px;top:45px;padding: 1px 5px;border-left: 5px solid white;line-height: 15px ">{{file.fname}}</span>
             <span style="position: absolute;right:20px;top:28px;background-color: white;color: #009688;padding: 1px 5px;border-radius: 5px;  ">
               发送消息
             </span>
           </div>
           <div class="body">
               <div class="fetch ">
                 <form class="layui-form layui-form-pane" action="" onsubmit="return false">
                   <div class="layui-inline">
                     <label class="layui-form-label">提取密码</label>
                     <div class="layui-input-inline">
                       <input type="text" name="number" autocomplete="off" class="layui-input" v-model="form.input">
                     </div>
                   </div>
                   <button v-if="!query.show"  style="margin-top: -5px;margin-left: 20px" class="layui-btn" @click="gainDetailFile()" >{{form.msg}}</button>
                   <button v-if="query.show"  style="margin-top: -5px;margin-left: 20px" class="layui-btn layui-btn-disabled"  >正在提取</button>

                 </form>
       <span>{{form.tip}}</span>
               </div>
           </div>
       </div>

      <div class="file  " v-if="files.show">
        <Navigation></Navigation>
        <div class="content">
          <div class="info">

           <span style="margin-left:-10px;padding-top:7px;padding-left:5px;height:25px;line-height: 25px;font-size:18px;padding-right: 100px ">
             <i class="iconfont icon-wenjianjia2" style="color:#009E94;font-size: 20px;left:11px;" v-show="file.fidCid==9"></i>
                <i class="iconfont icon-tupian" style="color:#009E94;" v-show="file.fidCid==3"></i>
                <i class="iconfont icon-dc-icon-zhongzidujiaoshou" style="color:#009E94;" v-show="file.fidCid==4"></i>
                <i class="iconfont icon-wendang" style="color:#009E94;" v-show="file.fidCid==5"></i>
                <i class="iconfont icon-shipin" style="color:#009E94;" v-show="file.fidCid==1"></i>
                <i class="iconfont icon-yasuobao" style="color:#009E94;" v-show="file.fidCid==7"></i>
                <i class="iconfont icon-yinle2" style="color:#009E94;" v-show="file.fidCid==2"></i>
                <i class="iconfont icon-yingyong" style="color:#009E94;" v-show="file.fidCid==8"></i>
                <i class="iconfont icon-qitatongyong" style="color:#009E94;" v-show="file.fidCid==6"></i>
             {{file.fname}}</span>
            <div style="position: absolute;right: 10px">
              <button type="button" class="layui-btn">保存到网盘</button>
              <button type="button" class="layui-btn layui-btn-normal">下载</button>
              <button type="button" class="layui-btn layui-btn-warm">发送消息</button>
              <img src="https://ss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/netdisk.1.22c937a6.ocMJQn2Ar3U1AxsoaZA5yw.jpg" alt="">
              <span style="font-size: 15px;font-weight: bold">{{file.user.username}}</span>
            </div>
          </div>
          <div style="position:relative;height:20px;width: 100%;margin-top: 15px;">
            <ul class="breadcrumb" style="text-align: left;height: 35px;">
              <li  v-for="(item,index) in nav" :class="{active:index==nav.length-1}" @click="clickNav(item.parentId,index)" >{{item.name}}</li>
            </ul>
          </div>
          <div style="margin-top:10px;">
            <table class="layui-table"  style="text-align: left;">
              <colgroup>
                <col width="6%">
                <col width="50%">
                <col width="20%">
              </colgroup>
              <thead>
              <tr>
                <th><input type="checkbox" name="" title="发呆" lay-skin="primary" v-model="allSelect.state">{{allSelect.msg}}</th>
                <th>文件名</th>
                <th>文件大小</th>
                <th>修改日期</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item,index) in files.data" @mouseleave="hoverIndex=-1">
                <td><input type="checkbox" name="" title="发呆" lay-skin="primary" v-model="box[index]" @mouseover="hoverIndex=index"></td>
                <td style="position: relative" @mouseover="hoverIndex=index">
                  <i class="iconfont icon-wenjianjia2" style="color:#009E94;font-size: 20px;left:11px;" v-show="item.fidCid==9"></i>
                  <i class="iconfont icon-tupian" style="color:#009E94;" v-show="item.fidCid==3"></i>
                  <i class="iconfont icon-dc-icon-zhongzidujiaoshou" style="color:#009E94;" v-show="item.fidCid==4"></i>
                  <i class="iconfont icon-wendang" style="color:#009E94;" v-show="item.fidCid==5"></i>
                  <i class="iconfont icon-shipin" style="color:#009E94;" v-show="item.fidCid==1"></i>
                  <i class="iconfont icon-yasuobao" style="color:#009E94;" v-show="item.fidCid==7"></i>
                  <i class="iconfont icon-yinle2" style="color:#009E94;" v-show="item.fidCid==2"></i>
                  <i class="iconfont icon-yingyong" style="color:#009E94;" v-show="item.fidCid==8"></i>
                  <i class="iconfont icon-qitatongyong" style="color:#009E94;" v-show="item.fidCid==6"></i>
                  <a href="javaScript:void(0)" @click="clickItem(item,index)"><span>{{item.fname}}</span></a>
                  <!--                <i class="iconfont icon-fenxiang" @click="sharez(item,index)" v-if="hoverIndex==index" style="font-size: 18px;position:absolute;left:70%;color:#009E94;"></i>-->
                </td>

                <td @mouseover="hoverIndex=index">{{item.size}}</td>
                <td @mouseover="hoverIndex=index">{{item.updateTime}}</td>
              </tr>

              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
  import Navigation from './Home/Navigation'
  import  {getFileByAdress,getFileDetail,getFiles} from '../api/file'
    export default {
        name: "Access",
      components:{Navigation},
        data(){
          return {
             access:{
               show:false
             },
            box:[],
            allSelect:{
               state:false,
               msg:"全选"
            },
            hoverIndex:-1,
            files:{
               show:false,
               data:[
                 {fid:1,fname:"ssss",fidCid:3,updatetime:"2019-2-23 29:22:22",size:5049}
               ]
            },
            file:null,
            form:{
               msg:"提取文件",
               input:'',
              tip:""
            },
            info:{
               show:true
            },
            query:{
               show:false
            },nav:[

            ],

          }
        },
      computed:{
        user: {
          get() {
            return this.$store.state.user;
          }
        }
      },created() {
            //获取参数发送请求
              var key=this.$route.params.key;
        this.$router.push({path:"/share/"+key});
              this.gainFile(key);


        },methods:{
        async gainFile(key){
          let {data:response}=await getFileByAdress(key);
          //模拟发送请求
          console.log(response);

          if(!response){
            console.log("不存在该文件");
            this.info.show=false;
            return;
          }
          //请求判断是否有提取过密码的session
          if(response.sharePassword){
            //结果存在，说明曾经访问过，直接返回结果显示文件列表
            console.log("存在 ");
            this.file=response;
            this.nav[0]={name:key,parentId:null};
            this.files.data=[response];
            this.access.show=false;
            this.info.show=false;
            this.files.show=true;
          }else{
            //结果不存在，显示提取密码界面
            this.info.show=false;
            this.access.show=true;
            this.file=response;
            console.log("不存在 ");
          }
        },
        async gainDetailFile(){
          this.form.tip="" ;
          this.query.show=true;
          let {data:response}=await getFileDetail(this.file.fid,this.form.input);
          this.query.show=false;
          if(response){
            var key=this.$route.params.key;
            this.nav[0]={name:key,parentId:null};
            this.files.data=[response];
            this.access.show=false;
            this.files.show=true;
          }else{
            this.form.tip="密码错误，注意大小写" ;
          }

        },async clickItem(item,index){
            if(item.isDir==1){
              this.$store.commit("setParentId",item.fid);
              this.files.data=null;
              var a={name:item.fname,parentId:item.fid};
             this.nav.push(a);
                  console.log(this.nav+"=======");
              this.changePath(item.fid);
              console.log(item);
               let {data:result} = await  getFiles(this.user.uid,item.fid);
             // console.log(result);
            this.files.data=result.data;
                  localStorage.setItem("nav",JSON.stringify(this.nav));

            }else{
               window.open('http://120.55.95.79/'+this.files.data[index].address);
            }
        },changePath(parentId){
          var path="/share";
          for(var i=0;i<this.nav.length;i++){
            path+=("/"+this.nav[i].name);
            console.log(path);
          }
          this.$router.push({path:path,query:{pid:parentId}});
        },
        async clickNav(parentId,index){
          if(!parentId){
            this.files.data=[this.file];
            var key=this.nav[0].name;
            this.nav=[];
            this.nav[0]={name:key,parentId:null};
            this.$router.push({path:"/share/"+key});
            return ;
          }
          this.nav=this.nav.slice(0,index+1);
          console.log(this.nav);
          this.$store.commit("setParentId",parentId);
          this.files.data=null;
          this.changePath(parentId);
          let {data:result} = await  getFiles(this.user.uid,parentId);
          console.log(result);
          this.files.data=result.data;

          // this.$route.params.参数名
        }



      }
    }
</script>

<style scoped>
#access{
  width:100%;
  height:100vh;
  /*background-color: #EEF2F6;*/
}

  .paner{
    position: fixed;
    width:460px;
    height:200px;
    background-color: white;
    box-shadow: -1px 1px 5px rgba(0,0,0,0.1);
    border-radius: 5px;
    top:47%;
    left:50%;
    transform: translate(-50%,-50%);

  }

  .tip{
    position: fixed;
    width:460px;
    height:150px;
    background-color: white;
    box-shadow: -1px 1px 5px rgba(0,0,0,0.1);
    border-radius: 5px;
    top:47%;
    left:50%;
    z-index:999;
    text-align: left;
    padding-left:20px;
    transform: translate(-50%,-50%);
  }

.paner .head{
   background-color: #009688;
  height: 80px;
  width: 100%;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  display: flex;
  flex-direction: row;
  color: white;
  padding-top: 19px;
  padding-left: 20px;
}

.paner .head img{
  width:44px;
  height:44px;
  border-radius: 50%;
}

.paner .head span{
  margin-left: 10px;
}


.paner .body .fetch{
  margin-top: 40px;
}

  .flag{
      display: block;
      font-size: 30px;
      font-weight: bolder;
      margin-top: -70px;
      position: absolute;
     left: 50%;
     transform: translate(-50%,0);
    color: white;
  }
.file{
  position: relative;
}

.file .content{
  position: fixed;
     background-color: white;
    width:98%;
  height: 100vh;
  left:1%;
    margin: auto;
    top:70px;
  }

.file .content .info{
  display: flex;
  flex-direction: row;
  padding: 10px 20px;
  color: black;
}

.file .content .info img{
  width: 30px;
  height: 30px;
  border-radius: 50%;


}
.file .content .info span{
  margin-left: 10px;
}
</style>
