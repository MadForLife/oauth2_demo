import { useEffect, useState } from "react";
import axios from "axios";

const Protected = ({ token }) => {
  const [hasFetched, setHasFetched] = useState(false);

  useEffect(() => {
    if (!token || hasFetched) return;

    const fetchData = async () => {
      try {
        const config = {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        };

        const { data } = await axios.get(
          "http://localhost:8080/api/example",
          config
        );
        console.log("Response:", data);

        setHasFetched(true);
      } catch (error) {
        console.error("Error:", error);
      }
    };

    fetchData();
  }, [token, hasFetched]);

  return <div>Protected Content</div>;
};

export default Protected;

// import { useEffect, useRef } from "react";
// import axios from "axios";

// const Protected = ({ token }) => {
//   const isRun = useRef(false);

//   useEffect(() => {
//     if (isRun.current) return;

//     isRun.current = true;

//     const config = {
//       headers: {
//         Authorization: `Bearer ${token}`,
//       },
//     };

//     if (token) {
//       axios
//         .get("http://localhost:8080/api/example", config)
//         .then((res) => console.log("Response:", res.data))
//         .catch((err) => console.error("Error:", err));
//     }
//   }, []);

//   return <div>Protected Content</div>;
// };

// export default Protected;

// import {useEffect, useRef} from "react";
// import axios from "axios";
// import keycloak from "keycloak-js/lib/keycloak.js";
//
// const Protected = () => {
//
//     const isRun = useRef(false);
//
//     useEffect(() => {
//         if (isRun.current) return;
//
//         isRun.current = true;
//
//         axios
//             .get("http://localhost:8080/api/example")
//             .then((res) => console.log(res.data))
//             .catch((err) => console.error(err));
//     }, []);
//
//     return <div>Protected</div>
// }
//
// export default Protected;
