<template>
  <div class="col-lg-12 col-md-6 customize-color"> <!-- see main.css -->
    <h4>Name:{{ angebot.name }}</h4>
    <h4>Angebot: {{ angebot.id }}</h4>
    <input 
    	type="text" 
    	name="angebotId" 
    	v-model="inputAngebotId" 
    	id="angebotId" 
    	data-cy="angebotId"
      size="60"
    />
    <button v-on:click="getAngebotForId()">Get Angebot for Id</button>
    <br />
    <input 
      value=""
      type="text"
      name="angebotName"
      id="angebotName"
      data-cy="angebotName"
      v-model="angebot.name"
      size="60"
    />
    <div v-if="v$.name.$anyDirty && v$.name.$invalid">
      <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
    </div>
    <button id="buttonSave" v-show="isAngebotValid" v-on:click="save()">Save</button>
    <button id="changeState" v-on:click="changeState()">changeState</button>
    <br />
  </div>
</template>
<style>
.customize-color{
  background-color: aliceblue !important;
  color: orangered;
}

</style>

<script lang="ts" src="./Angebot.ts"></script>