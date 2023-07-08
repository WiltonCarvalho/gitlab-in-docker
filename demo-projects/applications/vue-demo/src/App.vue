<template> 
  <img alt="Vue logo" src="./assets/logo.png"> 
  <h1>Welcome to Your Vue.js App</h1> 
  <h1>API Endpoint: {{ msg }}</h1> 
</template> 
<script> 
export default { 
  name: "API Endpoint", 
  data() { 
    return { 
      msg: process.env.VUE_APP_API_ENDPOINT 
    }; 
  } 
} 
</script> 
<style> #app { 
  font-family: Avenir, Helvetica, Arial, sans-serif; 
  -webkit-font-smoothing: antialiased; 
  -moz-osx-font-smoothing: grayscale; 
  text-align: center; 
  color: #2c3e50; 
  margin-top: 60px; 
  } 
</style> 

