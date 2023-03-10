var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        cardType:"none",
        cardColor:"none",
        amount: 0,
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
        create: function(event){
            event.preventDefault();
            if(this.cardType == "none" || this.cardColor == "none"){
                this.errorMsg = "You must select a card type and color";  
                this.errorToats.show();
            }else{
                let config = {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }
                axios.post(`/api/clients/current/cards?cardType=${this.cardType}&cardColor=${this.cardColor}&amount=${this.amount}`,config)
                .then(response => window.location.href = "/web/cards.html")
                .catch((error) =>{
                    this.errorMsg = error.response.data;  
                    this.errorToats.show();
                })
            }
        }
    },
    mounted: function(){
        this.verifyUser();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})