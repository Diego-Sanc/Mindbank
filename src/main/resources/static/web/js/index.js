var app = new Vue({
    el:"#app",
    data:{
        email:"",
        password:"",
        firstName: "",
        lastName: "",
        errorToats:null,
        errorMsg: "",
        showSignUp: false,
    },
    methods:{
        signIn: function(event){
            event.preventDefault();
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/login',`email=${this.email}&password=${this.password}`,config)
                .then(response => {
                    axios.get("/api/verification")
                        .then((response) => {
                            if (response.data=="verified") window.location.href="/web/accounts.html"
                            else window.location.href="/web/verificacion-code.html";
                        })
                })
                .catch(() =>{
                    this.errorMsg = "Sign in failed, check the information"
                    this.errorToats.show();
                    //window.location.href="/web/accounts.html";
                })
        },
        signUp: function(event){
            event.preventDefault();
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,config)
                .then(() => { this.signIn(event) })
                .catch(() =>{
                    this.errorMsg = "Sign up failed, check the information"
                    this.errorToats.show();
                })
        },
        showSignUpToogle: function(){
            this.showSignUp = !this.showSignUp;
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})