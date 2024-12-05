import './App.css'
import useAuth from "./hooks/keycloakConfig.jsx";

import Protected from "./components/Protected.jsx"
import Public from "./components/Public.jsx"

function App() {
    const [isLogin, token] = useAuth();
    return isLogin ? <Protected token={token} /> : <Public />;
}

export default App
