<template>
    <div >
      <div id="files" class="col-sm-12 col-md-9 col-lg-8 col-lg-offset-1">
           <div class="operation">
               <button class="layui-btn layui-btn-info" style="position: relative;">上传文件 <input
                 type="file" multiple style="position: absolute;top:0px;left:0px;opacity: 0;" id="filez"></button>
             <button class="layui-btn layui-btn-warm" style="position: relative;" >上传文件夹
               <input
                 type="file" webkitdirectory style="position: absolute;top:0px;left:0px;opacity: 0;" id="dirz">
             </button>
<!--             <button class="layui-btn layui-btn">下载</button>-->
               <button class="layui-btn layui-btn-warm ">新建文件夹</button>
             <button class="layui-btn layui-btn-danger" @click="deletes()">删除</button>
           </div>
          <div style="position:relative;height:20px;width: 100%;">
            <ul class="breadcrumb" style="text-align: left;">
              <li  v-for="(item,index) in nav" :class="{active:index==nav.length-1}" @click="clickNav(item.parentId,index)">{{item.name}}</li>
            </ul>
          </div>
        <div v-if="files.data==null" style="margin-top: 50px">
          Loading......
        </div>
        <div v-if="files.data!=null&&files.data.length==0" style="margin-top: 50px">
          {{message}}
        </div>
       <div id="dataTb" v-if="files.data!=null&&files.data.length>0">
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
               <i class="iconfont icon-fenxiang" @click="sharez(item,index)" v-if="hoverIndex==index" style="font-size: 18px;position:absolute;left:70%;color:#009E94;"></i>
             </td>

             <td @mouseover="hoverIndex=index">{{item.size}}</td>
             <td @mouseover="hoverIndex=index">{{item.updateTime}}</td>
           </tr>

           </tbody>
         </table>
       </div>

      </div>
      <UpFiles @close="upFiles.show=false" @data="changeData" :show="upFiles.show"></UpFiles>
      <UpDir @close="upDir.show=false" @data="changeData" :show="upDir.show"></UpDir>
      <Dialog :show="dialog.show" :msg="dialog.msg"></Dialog>
      <Share :share="share" @data="changeData" @close="share.show = false"></Share>
    </div>
</template>

<script>
  import UpFiles from '../tool/upFiles'
  import UpDir from '../tool/upDir'
  import Dialog from '../tool/Dialog'
  import Share from '../tool/Share'
  import {getFiles,deleteFile,deleteDir,getSpace} from '../../api/file.js'
    export default {
        name: "Files",
      components:{UpFiles,UpDir,Dialog,Share},
      computed: {
        user: {
          get() {
            return this.$store.state.user;
          }
        },
        query:{
          get(){
            return this.$store.state.query;
          }
        }
      },

      data(){
          return{
            upFiles:{
              show:false
            },
            upDir:{
              show:false
            },
            files:{
              data:null,
              parentId:0
            },
            nav:[
              {name:"All",parentId:0}

            ],
            allSelect:{state:false,msg:"全选"},
            box:[],
            selected:[],
            dialog:{
              show:false,
              msg:""
            },
            error:[],
            message:"这里还没有文件哟，赶快上传把！！",
            hoverIndex:-1,
            share:{
              show:false,
              data:null
            }

          }
      },
      created(){
        var path=this.$route.path;
        var parentId=this.$route.query.pid;
        console.log("||||||||||||",path);
        if(path){

          var nextpath=path.substring(5);
          if(localStorage.getItem("nav")&& parentId!=0&&path!="/All"&&path!="/"){
            this.nav=JSON.parse(localStorage.getItem("nav"));
          }

          this.initData(parentId);
        }else{
          this.initData();

        }

      },
      watch:{
          $router(newval,oldval){
            console.log("路有变化了"+newval);
          },
        allSelect:{
            handler(newval,oldval){
              if(newval.state){
                for (var i=0 ;i<this.box.length;i++) {
                  this.box[i]=true;
                }
                this.allSelect.msg="不全选";
              }else{
                for (var i=0 ;i<this.box.length;i++) {
                  this.box[i]=false;
                }
                this.allSelect.msg="全选";
              }
            },deep:true,immediate:false
        },
        files:{
            handler(newval,oldval){
             if(this.files.data){
               this.box.length=this.files.data.length;
             }
            },deep:true,immediate:false
        },
        query:{
            handler(newval,oldval){
              this.nav=this.nav.slice(0,1);
                this.files.data=newval;
                 this.$router.push({path:'/'});
                 if(newval!=null&&newval.length==0){
                   this.message="没有查询到结果";
                 }
            }
        },
        user:{
           handler(newval,oldval){
               var parentId=this.$route.query.pid;
               this.initData(parentId);
           },deep:true,immediate:false
        }
      },
      mounted() {

        layui.use('element', function(){
          var element = layui.element;
        });
        layui.use('form', function(){
          var form = layui.form;
        });
          this.uploadFiles();
          this.uploadDir();
      },methods:{
          uploadFiles(){
            $("#filez").bind("change",()=>{
              this.$store.commit("setFiles",$("#filez")[0].files);
              this.upDir.show=false;
              this.upFiles.show=true;
            });
          },
        uploadDir(){
          $("#dirz").bind("change",()=>{
            this.$store.commit("setFiles",$("#dirz")[0].files);
            console.log($("#dirz")[0].files);
            this.upFiles.show=false;
            this.upDir.show=true;
          });
        },
        async initData(pid){
          if(!pid){
            pid=0;
          }
          this.$store.commit("setParentId",pid);
          this.files.data=null;
          let {data:result} = await  getFiles(this.user.uid,pid);
          console.log(result);
          this.files.data=result.data;

        },
        async changeData(){
          this.files.data=null;
          let {data:result} = await  getFiles(this.user.uid,this.$store.state.parentId);
          console.log(result);
          this.files.data=result.data;
        },
        async clickItem(item,index){
          console.log(item)
            if(item.isDir==1){
                this.$store.commit("setParentId",item.fid);
                this.files.data=null;
                var a={name:item.fname,parentId:item.fid};
                this.nav.push(a);
                console.log(this.nav+"=======");
              this.changePath(item.fid);
                let {data:result} = await  getFiles(this.user.uid,item.fid);
              // console.log(result);
              this.files.data=result.data;
              localStorage.setItem("nav",JSON.stringify(this.nav));

            }else{
              window.open('http://120.55.95.79/'+this.files.data[index].address);
            }
        },
        async sharez(item,index){
           this.share.data=this.files.data[index];
           this.share.show=true;
           console.log(this.share);
        },
        async clickNav(parentId,index){
           this.nav=this.nav.slice(0,index+1);
           console.log(this.nav);
           this.$store.commit("setParentId",parentId);
           this.files.data=null;
          this.changePath(parentId);
           let {data:result} = await  getFiles(this.user.uid,parentId);
           console.log(result);
           this.files.data=result.data;

          // this.$route.params.参数名
        },changePath(parentId){
          var path="";
          for(var i=0;i<this.nav.length;i++){
            path+=("/"+this.nav[i].name);
            console.log(path);
          }
          this.$router.push({path:path,query:{pid:parentId}});
        },
        async deletes(){
            var total=0;
            this.selected=[];
            for (var i=0;i<this.box.length;i++){
               if(this.box[i]){
                 this.selected.push(i);
                 total++;
               }
            }
            if(total==0){
              alert("请勾选要删除的项")
            }else{
              //执行删除
              var success=0;
              var error=0;
              for(var i=0;i<this.selected.length;i++){
                  this.dialog.show=true;
                  if(this.files.data[this.selected[i]].isDir){
                    this.dialog.msg="正在删除:进度"+(i+1)+"/"+this.selected.length+" | 任务ID:folder->"+this.files.data[this.selected[i]].fid;
                    let {data:result}=await deleteDir(this.files.data[this.selected[i]].fid,this.$store.state.user.uid);
                    if(result.success){
                      success+=1;
                    }else{
                      error+=1;
                      this.error.push({fid:this.files.data[this.selected[i]].fid,msg:result.msg})
                    }
                  }else{
                   // alert("文件" )

                    this.dialog.msg="正在删除:进度"+(i+1)+"/"+this.selected.length+" | 任务ID:file->"+this.files.data[this.selected[i]].fid;
                    let {data:result}=await deleteFile(this.files.data[this.selected[i]].fid);
                    if(result.success){
                      success+=1;
                    }else{
                      error+=1;
                      this.error.push({fid:this.files.data[this.selected[i]].fid,msg:result.msg})
                    }

                  }
              }

              if(this.error.length!=0){
                var info="";
                for(var i=0;i<this.error.length;i++){
                  info+="fid:"+this.error[i].fid+"->"+this.error[i].msg+"|";
                }
                this.dialog.msg="总任务:"+(success+error)+" | 成功:"+success+" | 失败:"+error+" | 失败信息："+info;
              }else{
                this.dialog.msg="总任务:"+(success+error)+" | 成功:"+success+" | 失败:"+error;
              }
              this.box=[];
              this.error=[];
              this.allSelect.state=false;
              let a=setTimeout(()=>{
                this.dialog.show=false;

                clearTimeout(a);
              },10000);
              this.changeData();
              this.spacez();
            }
        },
        async spacez(){

          let {data:data}=await getSpace();
          console.log(data.data);
          this.$store.commit("setSpace",data.data);
        }
      }
    }
</script>

<style scoped>
  html{
     height: 100%;
  }
 body{
    height: 100%;
  }
#files{
  background-color: rgba(255,255,255,0.95);
  /*float: left;*/
  padding: 10px 10px;
  position: relative;
  height: 90vh;
  overflow: auto;
  /*margin-left: -10px;*/
  margin-bottom: 20px;


}
#dataTb{
  margin-top: 15px;
  overflow: auto;
  height: 80vh !important;
}

  #dataTb i {
    font-size: 25px;
    position: absolute;
    left:10px;
    top:13px;
  }
  #dataTb span {

    margin-left:30px
  }
.operation{
  display: flex;
  flex-direction: row;

}


.operation button{
  /*margin-left: 10px;*/
}

  .navs{
    margin-top: 10px;
    text-align: left;
    margin-left: 10px;
  }

  .data table{
    /*height: 800px;*/
  }

  table a{
    color:#666666;
  }
  table a:hover{
    color: #009688;
  }
</style>
