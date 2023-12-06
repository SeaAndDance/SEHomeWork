<template>
  <div class="container">
    <div class="header">
      <img src="../asset/jpg/headphoto.jpg" alt="User Photo" class="profile-photo"/>
      <div class="header-text">Room: 717</div>
      <div id="clock" class="clock"></div>
    </div>

    <!-- 侧边栏 -->
    <div class="sidebar"></div>

    <button class="image-air-button">
      <img src="../asset/jpg/air.jpg" alt="Air" class="sidebar-image_air"/>
    </button>
    <div class="sidebar-text_air">空调调节</div>

    <button class="image-bill-button">
      <img src="../asset/jpg/bill.jpg" alt="Bill" class="sidebar-image_bill"/>
    </button>
    <div class="sidebar-text_bill">账单查询</div>


    <div class="lineC1"></div>
    <div class="lineC2"></div>


    <div class="main-content">
      <div class="content"></div>

      <!-- 电源开关 -->
      <button class="power-button">
        <img src="../asset/jpg/path/to/power-switch-image.jpg" alt="Power Switch" class="power-switch-image-trigger"/>
      </button>
      <img src=". /power_switch.jpg" alt="Power Off" class="power-switch-hidden-image"/>
      <img src="./power_switch2.jpg" alt="Power On" class="power-switch-hidden-image2"/>


      <!-- 房间温度 -->
      <img src="./room_temp.jpg" alt="Room Temperature" class="room_temp"/>
      <div class="room_temperature-display">28℃</div>

      <!-- 主温度圆形表 -->
      <img src="./main_temp.jpg" alt="Main Temperature" class="main_temp"/>
      <div class="big_temperature-display">20</div>

      <!-- 调节温度加减形状 -->
      <img src="./adjust_temp.jpg" alt="Adjust Temperature" class="adjust_temp"/>
      <button id="small-switch-image-trigger" class="small-switch-image-trigger"></button>
      <button id="plus-switch-image-trigger" class="plus-switch-image-trigger"></button>

      <!-- 风速形状 -->
      <img src="./wind_speed.jpg" alt="Wind Speed" class="wind_speed"/>
      <button id="small-wind-trigger" class="small-wind-trigger"></button>
      <button id="plus-wind-trigger" class="plus-wind-trigger"></button>

      <div class="wind-speed-display">中档</div>

      <!-- 模式 -->
      <button class="zhileng-button">
        <img src="./zhileng.jpg" alt="Zhileng" class="zhileng"/>
      </button>
      <button class="zhire-button">
        <img src="./zhire.jpg" alt="Zhire" class="zhire"/>
      </button>
      <button class="songfeng-button">
        <img src="./songfeng.jpg" alt="Songfeng" class="songfeng"/>
      </button>
      <button class="auto-button">
        <img src="./auto.jpg" alt="Auto" class="auto"/>
      </button>

      <!-----直线框----->
      <div class="lineH1"></div>
      <div class="lineH2"></div>
      <div class="lineS1"></div>
      <div class="lineS2"></div>
      <div class="lineS3"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {defineComponent} from 'vue'


</script>

<style scoped>

</style>