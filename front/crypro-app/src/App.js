import {createMuiTheme, ThemeProvider} from "@material-ui/core/styles";
import orange from "@material-ui/core/colors/orange";
import React from "react";
import {Route, Switch} from "react-router-dom";
import VideoPage from "./pages/VideoPage";
import HomePage from "./pages/HomePage";
import withStyles from "@material-ui/core/styles/withStyles";
import Container from "@material-ui/core/Container";
import {AuthProvider, useAuthState} from "./context";
import {SnackbarProvider} from "notistack";
import * as locales from '@material-ui/core/locale';
import PrimarySearchAppBar from "./layouts/AppBar";
import AuthPage from "./pages/AuthPage";
import NewVideoPage from "./pages/NewVideoPage";
import NotFoundPage from "./pages/NotFoundPage";
import ProfilePage from "./pages/ProfilePage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import UserPage from "./pages/UserPage";
import pink from "@material-ui/core/colors/pink";
import CssBaseline from "@material-ui/core/CssBaseline";

const theme = createMuiTheme({
  palette: {
      primary: orange,
      secondary: pink,
      type: 'dark',
      text: {
          primary: "#ffffff",
          secondary: "#b8b8b8"
      }
  },
  breakpoints: {
    values: {
      xs: 0,
      sm: 600,
      md: 960,
      lg: 1280,
      xl: 1920
    },
  }
}, locales['ruRU']);

const StyledContainer = withStyles({
    root: {
        marginTop: 12
    }
})(Container);


function Routing() {
    const userDetails = useAuthState();
    const auth = userDetails.token !== '';

    return (
        <Switch>
            <Route path="/video/:videoToken" children={<VideoPage />} />
            <Route path="/user/:userId" children={<UserPage />} />
            <Route exact path="/" children={<HomePage />} />
            {
                !auth && <Route path="/auth" children={<AuthPage />} />
            }
            {
                !auth && <Route path="/reset" children={<ResetPasswordPage />} />
            }
            {
                auth && <Route path="/profile" children={<ProfilePage />} />
            }
            {
                auth && <Route path="/new" children={<NewVideoPage />} />
            }
            <Route children={<NotFoundPage />} />
        </Switch>
    )
}


function App() {

    return (
        <AuthProvider>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <SnackbarProvider maxSnack={3}>
                    <PrimarySearchAppBar />
                    <StyledContainer maxWidth="lg">
                        <Routing />
                    </StyledContainer>
                </SnackbarProvider>
            </ThemeProvider>
        </AuthProvider>
    );
}

export default App;
