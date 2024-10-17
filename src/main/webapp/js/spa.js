const env = {
    apiHost: "http://localhost:8080/JavaWebPv221_war_exploded",
};

function confirmAction({ title, text, confirmButtonText, cancelButtonText, onConfirm, onCancel }) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: "btn sweet-btn",
            cancelButton: "btn sweet-btn red"
        },
        buttonsStyling: false
    });

    swalWithBootstrapButtons.fire({
        title: title || "Are you sure?",
        text: text || "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: confirmButtonText || "Yes, delete it!",
        cancelButtonText: cancelButtonText || "No, cancel!",
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // Если пользователь подтвердил действие
            if (onConfirm) {
                onConfirm();
            }
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Если пользователь отменил действие
            if (onCancel) {
                onCancel();
            }
        }
    });
}

function request(url, params){
    if(url.startsWith('/') ){
        url = env.apiHost + url;
    }
    // if( typeof params.headers === "undefined"){
    //     params = {
    //         ...params,
    //         headers: {
    //             Authorization: `Bearer ${params.headers.Authorization}`
    //         }
    //     }
    // }
    // else if ( typeof params.headers.Authorization === "undefined" ){
    //
    // }


    return new Promise((resolve, reject) => {
        fetch( url, params )
            .then(r=>r.json())
            .then(j =>{
                if (j.status.isSuccessful ) {
                    resolve(j.data);
                }
                else {
                    reject(j.data);
                }
            })
    })


}

const initialState = {
    auth: {
        token: null,
        role: null
    },
    page: "home",
    shop: {
        categories: [ ]
    },
    cart: [],
};

function reducer(state, action) {
    switch( action.type ) {
        case 'auth' :
            return { ...state,
                auth: {
                    ...state.auth,
                    token: action.payload.token,
                    role: action.payload.role
                }
            };
        case 'cart' :
            return { ...state,
                cart: action.payload,
            };
        case 'navigate' :
            // console.log("navigate " + action.payload);
            window.location.hash = action.payload;
            return { ...state,
                page: action.payload
            };
        case 'setCategory' :
            return { ...state,
                shop: {
                    ...state.shop,
                    categories: action.payload
                }
            };
        default: throw Error('Unknown action.');
    }
}

const StateContext = React.createContext(null);

function Spa() {
    const [state, dispatch] = React.useReducer( reducer, initialState );
    const [login, setLogin] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [error, setError] = React.useState(false);
    const [isAuth, setAuth] = React.useState(false);
    const [resource, setResource] = React.useState("");
    const loginChange = React.useCallback( (e) => setLogin( e.target.value ) );
    const passwordChange = React.useCallback( (e) => setPassword( e.target.value ) );

    const authClick = React.useCallback( () => {
        const credentials = btoa( login + ":" + password );
        fetch("auth", {
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + credentials
            }
        }).then(r => r.json()).then( j => {
            if( j.status.isSuccessful) {
                window.sessionStorage.setItem( "token221", JSON.stringify( j.data ) );
                setAuth(true);
            }
            else {
                setError( j.data );
            }
        });
    } );
    const exitClick = React.useCallback( () => {
        window.sessionStorage.removeItem( "token221" );
        setAuth(false);
    });
    const resourceClick = React.useCallback( () => {
        const data = window.sessionStorage.getItem( "token221" );
        if( ! data ) {
            alert( "Запит ресурсу в неавторизованому режимі" );
            return;
        }
        fetch("spa", {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + JSON.parse(data.token).tokenId,
            }
        }).then(r => r.json()).then( j => {
            setResource( JSON.stringify(j) );
        });

    });
    const checkToken = React.useCallback( (forceAuth) => {
        let data =  JSON.parse(window.sessionStorage.getItem( "token221" ));
        if(data) {
            const token = data.token;

            if( new Date(token.exp) < new Date() ) {
                exitClick();
            }
            else {
                if(forceAuth) {
                    setAuth(true);
                    dispatch({ type: 'auth', payload: data });
                }
            }
        }
        else {
            setAuth(false);
        }

    });
    const hashChanged = React.useCallback( () => {
        const hash = window.location.hash;
        if( hash.length > 1 ) {
            dispatch( { type: 'navigate', payload: hash.substring(1) } );
        }
    } );
    const loadCart = React.useCallback(() => {
        request(`/shop/cart`, {
            headers: {
                'Authorization': 'Bearer ' + state.auth.token.tokenId
            },
        })
            .then(data => dispatch({type: 'cart', payload: data}))
            .catch(console.error);
    });
    const calculateTotalPrice = React.useCallback(()=>{
        return state.cart.reduce( (total, item) => total + (item.product.price * item.quantity), 0);
    })



    React.useEffect(() => {
        if(state.auth.token != null){
            loadCart();
        }
        else {
            dispatch({type: 'cart', payload: []});
        }
    },[state.auth]);
    React.useEffect(() => {
        hashChanged();
        checkToken(true);

        window.addEventListener('hashchange', hashChanged);
        const interval = setInterval(()=>checkToken(false), 1000);

        if (state.shop.categories.length === 0) {
            fetch("shop/category")
                .then(r => r.json())
                .then(j => dispatch({type: 'setCategory', payload: j.data}));
        }

        return () => {
            clearInterval(interval);
            window.removeEventListener('hashchange', hashChanged);
        }
    }, []);

    const navigate = React.useCallback( (route) => {
        // console.log(route);
        // const action = { type: 'navigate', payload: route };
        dispatch( { type: 'navigate', payload: route } );
    });

    return <StateContext.Provider value={ {state, dispatch, loadCart, calculateTotalPrice} }>
        <h1>SPA</h1>
        { !isAuth &&
            <div>
                <b>Логін</b><input onChange={loginChange} /><br/>
                <b>Пароль</b><input type="password"  onChange={passwordChange} /><br/>
                <button onClick={authClick}>Одержати токен</button>
                {error && <b>{error}</b>}
            </div>
        }{ isAuth &&
        <div>
            <button onClick={resourceClick} className="btn light-blue">Ресурс</button>
            <button onClick={exitClick} className="btn indigo lighten-4">Вихід</button>
            <p>{resource}</p>
            <button className="btn" onClick={() => navigate('home')}>Home</button>
            <button className="btn" onClick={() => navigate('cart')}>Cart({state.cart.reduce((cnt, c) => cnt + c.quantity, 0)}/{calculateTotalPrice()})</button>
            {state.auth.role.name === "admin" &&
                <button className="btn" onClick={() => navigate('shop')}>Shop</button>
            }

            {state.page === 'home' && <Home/>}
            {state.page === 'shop' && <Shop/>}
            {state.page === 'cart' && <Cart/>}
            {state.page.startsWith('category/') && <Category id={state.page.substring(9)}/>}
            {state.page.startsWith('product/') && <Product id={state.page.substring(8)}/>}
        </div>
    }
    </StateContext.Provider>;
}

function Category({id}) {
    const {state, dispatch, loadCart} = React.useContext(StateContext);
    const [products, setProducts] = React.useState([]);
    const loadProducts = React.useCallback( () => {
        request(`/shop/product?categoryId=${id}`)
            .then(setProducts)
            .catch(err => {
                console.error(err);
                setProducts( null );
            });
    } );

    React.useEffect( () => {
        loadProducts();
    }, [id] );

    const addProduct = React.useCallback( (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        fetch(`${env.apiHost}/shop/product`, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + state.auth.token.tokenId
            },
            body: formData
        }).then(r => r.json())
            .then(j => {
                if( j.status.isSuccessful ) {
                    loadProducts();
                    document.getElementById("add-product-form").reset();
                }
                else {
                    alert( j.data );
                }
            });
    });

    const addCart = React.useCallback((id)=>{
        console.log(id);
        const userId = state.auth.token.userId;
        request("/shop/cart", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${state.auth.token.tokenId}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId,
                productId: id,
                cnt: 3
            })
        })
            .then(()=>loadCart())
            .catch(console.log);
    })

    return <div >
        <CategoriesList mode="ribbon"></CategoriesList>
        {products && <div>
            Category: {id}<br/>
            <b onClick={() => dispatch({type: 'navigate', payload: 'home'})}>До Крамниці</b>
            <br/>
            {products.map(p =>
                <div key={p.id}
                     className="shop-product"
                     onClick={() => dispatch({type: 'navigate', payload: 'product/' + (p.slug || p.id)})}>
                    <b>{p.name}</b>
                    <picture>
                        <img src={"file/" + p.imageUrl} alt="prod"/>
                    </picture>
                    <div className="row" >
                        <div className="col s9">
                            <strong>{p.price} грн</strong>&emsp;
                            <small>{p.description}</small>
                        </div>
                        <div className="col s3">
                            <a className="btn-floating cart-fab waves-effect waves-light red" onClick={
                                (e) => {
                                    e.stopPropagation();
                                    addCart(p.id)
                                }
                            }><i
                                className="material-icons">shopping_bag</i></a>
                        </div>
                    </div>
                </div>)}
            <br/>
            {state.auth.token && state.auth.role.name === "admin" &&
                <form id="add-product-form" onSubmit={addProduct} encType="multipart/form-data">
                    <hr/>
                    <input name="product-name" placeholder="Назва"/>
                    <input name="product-slug" placeholder="Slug"/><br/>
                    <input name="product-price" type="number" step="0.01" placeholder="Ціна"/><br/>
                    Картинка: <input type="file" name="product-img"/><br/>
                    <textarea name="product-description" placeholder="Опис"></textarea><br/>
                    <input type="hidden" name="product-category-id" value={id} />
                    <button type="submit">Додати</button>
                </form>}
        </div>}
        {!products && <div>
            <h2>Группа товаров не существует</h2>
        </div>}
    </div>
}

function Product({id}) {
    const [product, setProduct] = React.useState(null);

    React.useEffect( () => {
        request(`/shop/product?id=${id}`)
            .then(setProduct)
            .catch( err => {
                console.log(err);
                setProduct( null );
            });
    }, [id] );

    return <div>
        <h1>Сторінка товару</h1>

        {product && <div>
            <p>{product.name}</p>
        </div>}

        {!product && <div>
            <p>Шукаємо...</p>
        </div>}
        <hr/>
        <CategoriesList />
    </div>;
}

function Home() {
    const {state, dispatch} = React.useContext(StateContext);
    React.useEffect(() => {

    }, [] );
    return <React.Fragment>
        <h2>Home</h2>
        {state.auth.role.name === "admin" &&
            <button className="btn" onClick={() => dispatch( { type: 'navigate', payload: 'shop' } )}>До Адмінки</button>
        }
        <CategoriesList mode="table" />
    </React.Fragment>;
}

function CategoriesList({ mode }) {
    const { state, dispatch } = React.useContext(StateContext);

    const isTableMode = mode === "table";

    return (
        <div className={isTableMode ? "table-view" : "ribbon-view"}>
            {state.shop.categories.map(c => (
                <div
                    key={c.id}
                    className={isTableMode ? "shop-category-table" : "shop-category-ribbon"}
                    onClick={() => dispatch({type: 'navigate', payload: 'category/' + (c.slug || c.id)})}
                >
                    <b>{c.name}</b>
                    <picture>
                        <img src={"file/" + c.imageUrl} alt="grp"/>
                    </picture>
                    <p>{c.description}</p>
                </div>
            ))}
        </div>
    );
}

function Shop() {
    const {state, dispatch} = React.useContext(StateContext);

    const addCategory = React.useCallback((e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        fetch("shop/category", {
            method: 'POST',
            body: formData
        }).then(r => r.json()).then(console.log);
        // console.log(e);
    });

    return <React.Fragment>
        <h2>Shop</h2>
        <hr/>
        {state.auth.role.name === "admin" ? (
            <form onSubmit={addCategory} encType="multipart/form-data">
                <input name="category-name" placeholder="Категорія"/>
                <input name="category-slug" placeholder="Slug"/><br/>
                Картинка: <input type="file" name="category-img"/><br/>
                <textarea name="category-description" placeholder="Опис"></textarea><br/>
                <button type="submit">Додати</button>
            </form>
        ) : (
            <div>Hello</div>
        )
        }
    </React.Fragment>;
}

function Cart() {
    const {state, loadCart, calculateTotalPrice} = React.useContext(StateContext);
    const [total, setTotal] = React.useState(() => calculateTotalPrice());
    const changeQuantity = React.useCallback((cartItem, action) => {
        switch (action) {
            case'inc': updateCart(cartItem, 1); break;
            case'dec': updateCart(cartItem, -1); break;
            case'del': updateCart(cartItem, -cartItem.quantity); break;
        }
    });
    const updateCart = React.useCallback((cartItem, delta) => {
        if(Number(cartItem.quantity) + Number(delta) === 0
            && ! confirm(`Удалить из корзины? '${ cartItem.product.name}'?`)){
                return;
        }
        request(`/shop/cart`, {
            method: 'PUT',
            headers: {
                Authorization: 'Bearer ' + state.auth.token.tokenId,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                cartId: cartItem.cartId,
                productId: cartItem.productId,
                delta: delta
            })
        }).then(()=>loadCart())
            .catch(console.log);
    });

    const buyClick = React.useCallback(()=>{
        if(confirm( `Подтверждаете покупку на ${total}  грн?`)) {
            request(`/shop/cart?cart-id=${state.cart[0].cartId}&is-cancelled=false`, {
                method: 'PATCH',
                headers: {
                    Authorization: 'Bearer ' + state.auth.token.tokenId,
                    'Content-Type': 'application/json'
                }
            }).then(()=>loadCart())
                .catch(console.log);
        }
    });

    const cleanCart = React.useCallback(()=> {
        confirmAction({
            title: "Очистить корзину?",
            text: "Вы не сможете отменить это действие.",
            confirmButtonText: "Да",
            cancelButtonText: "Нет",
            onConfirm: () => {
                request(`/shop/cart?cart-id=${state.cart[0].cartId}&is-cancelled=true`, {
                    method: 'PATCH',
                    headers: {
                        Authorization: 'Bearer ' + state.auth.token.tokenId,
                        'Content-Type': 'application/json'
                    }
                }).then(() => loadCart())
                    .catch(console.log);
            },
            onCancel: () => {
            }
        });
    })

    React.useEffect(() => {
        setTotal(calculateTotalPrice());
    }, [state.cart])

    return <div className="container">
        <h3 className="center-align">Корзина</h3>
        {state.cart.length === 0 ? (
            <div className='center-align' style={{ padding: "10px" }}>
                <img src='https://xl-static.rozetka.com.ua/assets/img/design/modal-cart-dummy.svg'></img>
                <h4>Корзина пуста</h4>
                <p>Но это никогда не поздно исправить :)</p>
            </div>
        ) : (
            <div>
                <table className="highlight responsive-table">
                    <thead>
                    <tr>
                        <th>Изображение</th>
                        <th>Название</th>
                        <th>Цена (грн)</th>
                        <th>Количество</th>
                        <th>Действия</th>
                        <th>Цена</th>

                    </tr>
                    </thead>
                    <tbody>
                    {state.cart.map((c) => (
                        <tr key={c.productId}>
                            <td>
                                <img src={"file/" + c.product.imageUrl} alt="prod" width={55}/>
                            </td>
                            <td>{c.product.name}</td>
                            <td>{c.product.price}</td>
                            <td>{c.quantity}</td>
                            <td>
                                <button className="btn" onClick={() => {
                                    changeQuantity(c, "dec")
                                }}>-
                                </button>
                                <span> {c.quantity} </span>
                                <button className="btn" onClick={() => {
                                    changeQuantity(c, "inc")
                                }}>+
                                </button>
                                &emsp;
                                <button className="btn red" onClick={() => {
                                    changeQuantity(c, "del")
                                }}>x
                                </button>
                            </td>
                            <td>{c.product.price * c.quantity}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div className='cart-summary'>
                    <h3>Итого: {total} ₴</h3>
                    <button className='btn red' onClick={cleanCart}>
                        Очистить корзину
                    </button>
                    <button className='btn' onClick={buyClick}>
                        Оформить заказ
                    </button>

                </div>
            </div>
        )}
    </div>
}

ReactDOM
    .createRoot(document.getElementById("spa-container"))
    .render(<Spa/>);
