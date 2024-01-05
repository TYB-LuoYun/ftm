<template>
    <div v-if="show" id="dialog">
       <div class="dialog">
         {{msg}}
       </div>
    </div>
</template>

<script>
    export default {
        name: "Dialog",
        props:["show","msg"]
    }
</script>

<style scoped>
#dialog{
  height: 50px;
  line-height: 50px;
 font-size: 15px;
  padding: 0px 10px;
  position: fixed;
  z-index: 999;
  left:0px;
  bottom: 100px;
  background-color: rgba(0,0,0,0.8);
  border-right:5px solid #009688;
  color: white;

}
</style>
