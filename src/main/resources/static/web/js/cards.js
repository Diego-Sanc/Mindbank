var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        creditCards: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
        today: new Date().getTime(),
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
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
                this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT");
                this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT");
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        deleteCard: function(id){
            axios.delete(`/api/clients/current/cards?id=${id}`)
            .then((response) => this.getData())
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