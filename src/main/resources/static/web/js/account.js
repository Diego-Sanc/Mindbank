var app = new Vue({
    el:"#app",
    data:{
        accountInfo: {},
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
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/accounts/${id}`)
            .then((response) => {
                //get client ifo
                this.accountInfo = response.data;
                this.accountInfo.transactions.sort((a,b) => parseInt(b.id - a.id))
            })
            .catch((error) => {
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        },
    },
    mounted: function(){
        this.verifyUser();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})