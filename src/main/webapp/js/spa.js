function Spa(){
    const [isAuth, setIsAuth] = React.useState(false);
    const [error, setError] =  React.useState(false);
    const [resources, setResources] = React.useState("");
    const [login, setLogin] = React.useState("");
    const [password, setPassword] = React.useState("");
    const loginChange = React.useCallback((e)=>{
        setLogin(e.target.value)
    })
    const passwordChange = React.useCallback((e)=>{
        setPassword(e.target.value)
    })
    const resourceClick = React.useCallback(()=>{
        const token = sessionStorage.getItem("token221");
        if(!token){
            alert("Запрос ресурса в неавторизованом проекте")
            return;
        }
        fetch("spa", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": 'Bearer ' + JSON.parse(token).tokenId,
            }
        }).then(res => res.json() ).then(res=>{
            window.sessionStorage.setItem("userData", JSON.stringify(res.data));
        })
    })

    const authClick = React.useCallback(()=>{
        const credentials = btoa(`${login}:${password}`);
        fetch("auth", {
            method: "GET",
            headers: {
                'Authorization': 'Basic ' + credentials,
            }
        }).then(r=>r.json()).then( r => {
            console.log(r.data);
                if (r.status === "Ok") {
                    window.sessionStorage.setItem("token221", JSON.stringify(r.data));
                    setIsAuth(true);
                    resourceClick();
                }
                else {
                    setError(false);
                }
            }
        )
        console.log(credentials);
    })

    const exitClick = () => {
        window.sessionStorage.removeItem("token221");
        window.sessionStorage.removeItem("userData");

        setIsAuth(false);
    }

    const checkToken= ()=>{
        let token = sessionStorage.getItem("token221");
        if(token){
            token = JSON.parse(token);
            if(new Date(token.exp ) < new Date()){
                exitClick();
            } else {
                if(!isAuth) setIsAuth(true);

            }
        }
        else {
            setIsAuth(false);
        }
    }

    React.useEffect(()=>{
        checkToken();
        const interval = setInterval(checkToken, 1000);
        return () => clearInterval(interval);
    },[]);


    return <React.Fragment>
        <h1>Spa</h1>
        { !isAuth &&
        <div>
            <b>Логин</b><input onChange={loginChange} /><br/>
            <b>Пароль</b><input type="password" onChange={passwordChange} /><br/>
            <button onClick={authClick}> Получить токен</button>
            {error && <b>{error}</b>}
        </div>
        }{isAuth &&
        <div>
            <button onClick={resourceClick} className="btn purple"> Ресурс</button>
            <button onClick={exitClick} className="btn gray"> Выйти</button>
            <p>{resources}</p>
        </div>
    }
    </React.Fragment>
}

ReactDOM
    .createRoot(document.getElementById('spa-container'))
    .render(<Spa></Spa>)
