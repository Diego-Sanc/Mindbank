var app = new Vue({
    el:"#app",
    data:{
        dynaPIN: "{'000000', 60}",  //{ pin , timeLeft}
        barTimer: null,
        getDataTimer: null,
        errorToats: null,
        errorMsg: null,
    },

    methods:{
        verifyUser: function(){
            axios.get("/api/verification")
                .then((response) => {
                    if (response.data!="verified") window.location.href="/web/verificacion-code.html";
                })
                .catch(() =>{
                    this.errorMsg = "Error Verifying";
                    this.errorToats.show();
                    //window.location.href="/web/accounts.html";
                })
        },
        getData: function(){
            axios.get("/api/clients/current/dynaPIN")
                .then((response) => {
                    //get client ifo
                    this.dynaPIN = response.data;
                    console.log("data extraida")
                    clearInterval(this.getDataTimer);
                    this.getDataTimer = setInterval(this.getData,this.dynaPIN.timeLeft*1000);
                    })
                .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
                })

            },
        signOut: function(){
                    axios.post('/api/logout')
                    .then(response => window.location.href="/web/index.html")
                    .catch(() =>{
                        this.errorMsg = "Sign out failed";
                        this.errorToats.show();
            })
        },
        timer: function(){
            if (this.dynaPIN != null && this.dynaPIN.timeLeft>0) this.dynaPIN.timeLeft-=1;
        }
    },
    computed: {
        cssProps() {

              return {
                '--duration': this.dynaPIN.timeLeft
              }
            }

    },
    mounted: function(){
        this.verifyUser();
        this.getData();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.barTimer = setInterval(this.timer,1000);
    },
    updated: function(){



    }
})