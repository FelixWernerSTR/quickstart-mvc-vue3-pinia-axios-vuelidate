https://stackoverflow.com/questions/54955426/how-to-use-async-await-in-vue-js

I want to use async/await in Vue.js

Here is my code

created (){
    this.getA()
    console.log(2)
    this.getB() 
},
methods : {
    getA (){
        console.log(1)
    },
    getB (){
        console.log(3)
    }
}
It returns

1
2
3
But when I use it with axios, then

created (){
    this.getA()
    console.log(2)
    this.getB() 
},
methods : {
    getA (){
        $axios.post(`/getA`,params){
        .then((result) => {
            console.log(1)
        })
    },
    getB (){
        console.log(3)
    }
}
It returns

2
3
1

You have to use either then or await not both as shown below:

If using then

created () {
    this.getA().then((result) => {
            console.log(1)
            console.log(2)
            this.getB()
        })
},
methods : {
    getA () {
        return $axios.post(`/getA`,params);
    },
    getB (){
        console.log(3)
    }
}
If using await

async created (){
    await this.getA()
    console.log(1)
    console.log(2)
    this.getB() 
},
methods : {
    getA : async() => {
        return $axios.post(`/getA`,params);
    },
    getB : () => {
        console.log(3)
    }
}
Note that while calling getB() you don't need then or await since it is not asynchronous
