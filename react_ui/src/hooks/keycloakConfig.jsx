import { useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";

const client = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL,
  realm: import.meta.env.VITE_KEYCLOAK_REALM,
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID,
});

const useAuth = () => {
  const isRun = useRef(false);
  const [token, setToken] = useState(null); // extracting the token so we can include it in every api call
  const [isLogin, setLogin] = useState(false);

  useEffect(() => {
    if (isRun.current) return;

    isRun.current = true;
    client.init({ onLoad: "login-required" }).then((res) => {
      setLogin(res);
      setToken(client.token);
    });
  }, []);

  return [isLogin, token];
};

export default useAuth;
