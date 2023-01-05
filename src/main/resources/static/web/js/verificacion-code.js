var app = new Vue({
    el:"#app",
    data:{
        verificacion: null,
        errorToats: null,
        errorMsg: null,
    },

    methods:{
        getData: function(){
                   axios.get("/api/verification")
                               .then((response) => {
                                   //get client ifo
                                   if(response.data){
                                   window.location.href="/web/accounts.html"
                                   }
                               })
                    .catch((error) => {
                        this.errorMsg = "Error getting data";
                        this.errorToats.show();
                    })
                },
        verify: function(){
        let config = {
                        headers: {
                            'content-type': 'application/x-www-form-urlencoded'
                        }
                    }
                    axios.post(`/api/verification?codVerify=${this.verificacion},config)
                    .then(response =>
                    window.location.href="/web/accounts.html"
                    )
                    .catch((error) =>{
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
        }



    },
    mounted: function(){

        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    },

})