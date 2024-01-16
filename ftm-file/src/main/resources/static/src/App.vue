<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top:0px;
  /*background-color: red;*/
  width: 100%;
  height:100%;
}
body{
  background-image:url("../static/image/bg.jpg");

  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position-y: -20px;
  background-size:cover;
}


</style>
