function AuthNav () {

    const [isAuth, setIsAuth] = React.useState(false);
    const [userData, setUserData] = React.useState(null);
    const [contextPath, setContextPath] = React.useState("");

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
                if(!isAuth) {
                    setIsAuth(true);
                    setUserData(JSON.parse(sessionStorage.getItem("userData")));
                }
            }
        }
        else {
            setIsAuth(false);
        }
    }

    React.useEffect(()=>{
        fetch('spa',{
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(res => res.json() ).then(res=>{setContextPath (res.data)})
        checkToken();
    },[])


    return <React.Fragment>
        {isAuth && userData &&
            <div className="nav-user-info right">
                <p className="nav-addon">Hello, {userData.name}</p>
              <img src={`${contextPath}/file/${userData.avatar}`}
                  className="nav-addon nav-avatar"/>
            </div>
         }
    </React.Fragment>
}


ReactDOM
    .createRoot(document.getElementById('react-auth-container'))
    .render(<AuthNav/>);
