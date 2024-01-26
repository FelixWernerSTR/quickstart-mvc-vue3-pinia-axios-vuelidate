<template>
  <div class="hello">
    <h2>Name:{{ bar.name }}</h2>
    <h2>Bar: {{ bar.id }}</h2>
    <input type="text" name="barId" id="barId" data-cy="barId"/>
    <button v-on:click="getBarForId()">Get Bar for Id</button>
    <br />
    <input
      type="text"
      name="barName"
      id="barName"
      data-cy="barName"
      v-model="bar.name"
    />
    <div v-if="v$.name.$anyDirty && v$.name.$invalid">
      <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
    </div>
    <button v-on:click="save()">Save</button>
    <br />
  </div>
</template>

<script lang="ts" src="./Bar.ts"></script>