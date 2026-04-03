import { Box, Button, Typography } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router";
import { setCredentials } from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";

const ActvitiesPage = ({ onActivityAdded, refreshKey }) => {
  return (
    <Box
      sx={{
        display: "grid",
        gridTemplateColumns: { xs: "1fr", lg: "380px 1fr" },
        gap: 3,
      }}
    >
      <Box
        sx={{
          borderRadius: 3,
          p: { xs: 2, md: 3 },
          border: "1px solid rgba(255,255,255,0.14)",
          background: "rgba(255,255,255,0.05)",
          backdropFilter: "blur(6px)",
        }}
      >
        <Typography sx={{ color: "#d8f3ff", fontWeight: 700, mb: 1.5 }}>
          Add New Activity
        </Typography>
        <ActivityForm onActivityAdded={onActivityAdded} />
      </Box>

      <Box
        sx={{
          borderRadius: 3,
          p: { xs: 2, md: 3 },
          border: "1px solid rgba(255,255,255,0.14)",
          background: "rgba(255,255,255,0.05)",
          backdropFilter: "blur(6px)",
        }}
      >
        <Typography sx={{ color: "#d8f3ff", fontWeight: 700, mb: 2 }}>
          Recent Activities
        </Typography>
        <ActivityList refreshKey={refreshKey} />
      </Box>
    </Box>
  );
}

function App() {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [refreshKey, setRefreshKey] = useState(0);

  const handleActivityAdded = () => {
    setRefreshKey((prev) => prev + 1);
  };

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
    }
  }, [token, tokenData, dispatch]);

  return (
    <Router>
      {!token ? (
        <Box
          sx={{
            minHeight: "100vh",
            px: { xs: 2, md: 4 },
            py: { xs: 3, md: 5 },
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            background:
              "radial-gradient(circle at 15% 20%, #12365a 0%, #0d1b2a 48%, #070d15 100%)",
          }}
        >
          <Box
            sx={{
              width: "100%",
              maxWidth: 980,
              borderRadius: 4,
              p: { xs: 3, md: 5 },
              border: "1px solid rgba(255,255,255,0.16)",
              background:
                "linear-gradient(160deg, rgba(255,255,255,0.12) 0%, rgba(255,255,255,0.04) 100%)",
              backdropFilter: "blur(8px)",
              color: "#f5f8ff",
              display: "grid",
              gridTemplateColumns: { xs: "1fr", md: "1.2fr 0.8fr" },
              gap: 4,
              alignItems: "center",
            }}
          >
            <Box>
              <Typography
                sx={{
                  display: "inline-block",
                  px: 1.5,
                  py: 0.5,
                  borderRadius: 99,
                  fontSize: 12,
                  letterSpacing: "0.08em",
                  textTransform: "uppercase",
                  border: "1px solid rgba(255,255,255,0.25)",
                  mb: 2,
                }}
              >
                Fitness Tracker
              </Typography>
              <Typography
                variant="h2"
                sx={{
                  fontWeight: 800,
                  fontSize: { xs: "2rem", md: "3rem" },
                  lineHeight: 1.08,
                  mb: 2,
                }}
              >
                Modern training starts with clear tracking.
              </Typography>
              <Typography
                sx={{
                  color: "rgba(245,248,255,0.86)",
                  fontSize: { xs: "1rem", md: "1.1rem" },
                  maxWidth: 580,
                  mb: 3,
                }}
              >
                Log workouts, review your activity history, and stay consistent from one focused dashboard.
              </Typography>
              <Button
                variant="contained"
                size="large"
                onClick={() => logIn()}
                sx={{
                  px: 4,
                  py: 1.3,
                  borderRadius: 2,
                  fontWeight: 700,
                  textTransform: "none",
                  background: "linear-gradient(135deg, #2dd4bf 0%, #22c55e 100%)",
                  color: "#04210f",
                  "&:hover": {
                    background: "linear-gradient(135deg, #4de0cb 0%, #4ad176 100%)",
                  },
                }}
              >
                Get Started
              </Button>
            </Box>
            <Box
              sx={{
                p: 2.5,
                borderRadius: 3,
                border: "1px solid rgba(255,255,255,0.2)",
                backgroundColor: "rgba(255,255,255,0.06)",
              }}
            >
              <Typography sx={{ fontWeight: 700, fontSize: 18, mb: 1.5 }}>
                Why it works
              </Typography>
              <Typography sx={{ fontSize: 14, color: "rgba(245,248,255,0.82)", mb: 1 }}>
                Clean activity logging
              </Typography>
              <Typography sx={{ fontSize: 14, color: "rgba(245,248,255,0.82)", mb: 1 }}>
                Quick progress review
              </Typography>
              <Typography sx={{ fontSize: 14, color: "rgba(245,248,255,0.82)" }}>
                Secure sign-in with Keycloak
              </Typography>
            </Box>
          </Box>
        </Box>
      ) : (
        <Box
          sx={{
            minHeight: "100vh",
            px: { xs: 2, md: 4 },
            py: { xs: 2.5, md: 4 },
            background:
              "radial-gradient(circle at 10% 0%, #0f2d43 0%, #0b1d2d 45%, #06111c 100%)",
          }}
        >
          <Box sx={{ width: "100%", maxWidth: 1240, mx: "auto" }}>
            <Box
              sx={{
                mb: 3,
                borderRadius: 3,
                px: { xs: 2, md: 3 },
                py: { xs: 2, md: 2.5 },
                border: "1px solid rgba(255,255,255,0.16)",
                background: "rgba(255,255,255,0.07)",
                backdropFilter: "blur(6px)",
                display: "flex",
                alignItems: { xs: "flex-start", sm: "center" },
                justifyContent: "space-between",
                flexDirection: { xs: "column", sm: "row" },
                gap: 1.5,
              }}
            >
              <Box>
                <Typography sx={{ color: "#ffffff", fontSize: { xs: 24, md: 30 }, fontWeight: 800, lineHeight: 1.1 }}>
                  Activity Dashboard
                </Typography>
                <Typography sx={{ color: "rgba(230,247,255,0.82)", fontSize: 14 }}>
                  Track sessions, review metrics, and keep your momentum high.
                </Typography>
              </Box>
              <Button
                variant="contained"
                onClick={() => logOut()}
                sx={{
                  borderRadius: 2,
                  px: 2.5,
                  fontWeight: 700,
                  textTransform: "none",
                  background: "linear-gradient(135deg, #fca5a5 0%, #fb7185 100%)",
                  color: "#3a0415",
                  "&:hover": {
                    background: "linear-gradient(135deg, #fec0c0 0%, #fb8ca2 100%)",
                  },
                }}
              >
                Logout
              </Button>
            </Box>

            <Routes>
              <Route
                path="/activities"
                element={<ActvitiesPage onActivityAdded={handleActivityAdded} refreshKey={refreshKey} />}
              />
              <Route path="/activities/:id" element={<ActivityDetail />} />
              <Route path="/" element={token ? <Navigate to="/activities" replace /> : <div>Welcome! Please Login.</div>} />
            </Routes>
          </Box>
        </Box>
      )}
    </Router>
  )
}

export default App
