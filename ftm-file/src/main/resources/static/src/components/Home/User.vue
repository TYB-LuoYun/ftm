<template>
    <div>
      <div id="user" class="col-sm-12 col-md-2 col-lg-2">
        <div class="panl" style="margin-top: 0px;">
          <h4>我的网盘</h4>
          <ul>
            <li @click="query('','',-1)"  :class="{active:index==-1}">全部</li>
            <li @click="query(3,'',0)"  :class="{active:index==0}">图片</li>
            <li @click="query(5,'',1)" :class="{active:index==1}">文档</li>
            <li @click="query(1,'',2)"  :class="{active:index==2}">视频</li>
            <li @click="query(4,'',3)"  :class="{active:index==3}">种子</li>
            <li @click="query(2,'',4)"  :class="{active:index==4}">音乐</li>
            <li @click="query(8,'',5)"  :class="{active:index==5}">应用</li>
            <li @click="query(7,'',6)"   :class="{active:index==6}">压缩包</li>
            <li @click="query(6,'',7)"   :class="{active:index==7}">其它</li>
          </ul>
        </div>
        <div id="search" class="panl" style="padding: 0px 0px">
          <div style="background-color: #808080;height:80px;border-top-left-radius: 5px;border-top-right-radius: 5px;">
            <input v-model="key" @keyup.enter="query('',key)" type="text" placeholder="输入关键字查找" style="width: 80%;height: 40px;line-height:40px;padding-left:20px;border-radius: 50px;margin-top: 20px;border: 0px">
          </div>
          <ul >
            <li v-for="(item,index) in history" @click="clickHistory(item)">{{item}}</li>
          </ul>
          <div class="clearfloat">
          </div>
        </div>
        <div class="panl">
          <h4>磁盘容量</h4>
          <div style="height: 50px;padding-top: 10px;position: relative">
            <div class="progress progress-striped active" >
              <div   class="progress-bar progress-bar-success" role="progressbar"
                   aria-valuenow="10" aria-valuemin="0" aria-valuemax="100"
                   :style="{width: progress,height:'10px'}">
              </div>

            </div>
            <span style="position:absolute;top: 24px;right:0px;font-size: 10px;color: #009E94">{{spacemsg}}</span>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
  import {queryFiles,getSpace} from '../../api/file'
    export default {
        name: "User",
      mounted() {


//获取历史搜索
        if(localStorage.getItem("history")){
          this.history=JSON.parse(localStorage.getItem("history"));
        }
        //获取空间
        this.spacez();
      },
      data(){
          return {
            key:"",
            index:-2,
            history:[],
            progress:'0%',
            spacemsg:"读取中..."
          }
      },
      computed:{
          user:{
            get(){
              return this.$store.state.user;
            }
          },
          space:{
            get(){
              return this.$store.state.space;
            }
          }
      },
      watch:{
          space:{
            handler(newval,oldval){
               this.progress=100*newval.free/newval.total+"%";
               this.spacemsg= newval.free+"G / "+newval.total+"G";
            },deep:true
          }
      },
      methods:{
          async query(fidCid,key,index){
            if(fidCid){
               this.index=index;
            }
            if(key){
               if(this.history.length>10){
                 this.history=this.history.slice(0,10);
               }
               this.history.unshift(key);
               localStorage.setItem("history",JSON.stringify(this.history));
            }
             let {data:data} = await  queryFiles(this.user.uid,fidCid,key);
             console.log(data.data);
             this.$store.commit("setQuery",data.data);

          },
        async spacez(){

            let {data:data}=await getSpace();
            console.log(data.data);
            this.$store.commit("setSpace",data.data);
        },
        clickHistory(item){
            this.key=item;
            this.query('',item,-2);
        }
      }
    }
</script>

<style scoped>
  .active{
    color: #009E94;
  }
#user{
  /*padding: 0px 10px;*/
  /*background-color: rgba(255,255,255,0.95);*/

  margin-left: 10px;
  /*float: left;*/
}

.clearfloat {
  clear: both;
  height: 0;
  font-size: 1px;
  line-height: 0px;
}

  .panl{

    padding:1px 15px;
    border-radius: 5px;
    background-color: rgba(255,255,255,0.95);
    margin-top: 20px;
  }
.panl h4{
  text-align: left;
  border-left: 5px solid #009E94;
  background-color: #F2F2F2;
  height: 30px;
  line-height: 30px;
  padding-left: 10px;
}
.panl ul{
  margin-left: 6px;
}

.panl ul li{
  text-align: left;
  height:30px;
  line-height: 30px;
  padding-left: 10px;
  cursor: default;
}
.panl  ul li:hover{
  color: #009E94;
  background-color: #F2F2F2;
}

 #search ul li{
   float: left;
   /*background-color: #009E94;*/
 }
</style>
