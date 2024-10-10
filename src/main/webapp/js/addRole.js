
function AddRoleForm() {
    const [roleName, setRoleName] = React.useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        if (roleName && roleName.length > 2 ) {
            const formData = new FormData(e.target);

            fetch('role', {
                method: 'POST',
                body: formData
            }).then(r => r.json()).then(r => {
                console.log(r)
                if (r.status === "Ok") {
                    alert("Role added successfully");
                } else {
                    alert(`Error: ${r.data}`);
                }});
        }
    };

    return (
        <div className="container">
            <h5>Add New Role</h5>
            <form onSubmit={handleSubmit}>
                <div className="input-field">
                    <input
                        type="text"
                        name="name"
                        id="roleName"
                        value={roleName}
                        onChange={(e) => setRoleName(e.target.value)}
                        required
                    />
                    <label htmlFor="roleName">Role Name</label>
                </div>

                <button type="submit" className="btn waves-effect purple">
                    Add Role
                </button>
            </form>
        </div>
    );
};

ReactDOM
    .createRoot(document.getElementById('add-role-container'))
    .render(<AddRoleForm/>);
