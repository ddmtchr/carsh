import React, {useState} from "react";
import {Box, Button, TextField, Typography} from "@mui/material";
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../axiosConfig.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../stores/store.ts";
import {setUser} from "../stores/userSlice.ts";
import "../index.css"
import Djiga from '../assets/57892330c04b2155efb17694.png'
import Logo from '../assets/logo.png'
import {VerificationStatuses} from "../interfaces.ts";

const LoginForm: React.FC = () => {
    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [route, setRoute] = useState<string>('')
    // const [error, setError] = useState(false)
    const navigate = useNavigate();

    const dispatch = useDispatch<AppDispatch>();

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault()

        axiosInstance.post('api/auth/login', {
            username: username,
            password: password
        })
            .then((response) => {
                if (response.status === 200) {
                    localStorage.setItem('accessToken', response.data.jwt)
                    const fetchedUser = {
                        id: response.data.id,
                        role: response.data.role,
                        username: response.data.username
                    }
                    dispatch(setUser(fetchedUser))

                    if (fetchedUser.role === 'ROLE_ADMIN' || fetchedUser.role === 'ROLE_ACCIDENT_COMMISSAR') {
                        setRoute('/admin-page')
                    } else {
                        axiosInstance.get('api/document-verification/my', {})
                            .then(response => {
                                const verifiedVerificationApplications = response.data.filter((a: {
                                    status: VerificationStatuses;
                                }) => a.status === VerificationStatuses.VERIFIED)
                                verifiedVerificationApplications.length === 0 ? setRoute('/document-verification') : setRoute('/main-screen')
                            })
                            .catch(error => {
                                console.log(error)
                            })
                    }
                    navigate(route)
                }
            })
            .catch((error) => {
                // setError(true)
                console.log(error)
            })

    }

    // const handleNotificationCLose = () => {
    //     setError(false)
    // }


    return (
            <div className="form-container">
                <img src={Logo} alt="logo" className="logo-image"/>
                <Box
                    className="base-form"
                    component="form"
                    onSubmit={handleLogin}
                    sx={{display: 'flex', alignItems: 'center', gap: 3, width: '50vw'}}
                >
                    <Typography variant="h5" className="header-text">Добро пожаловать!</Typography>
                    <TextField
                        label="Логин"
                        variant="outlined"
                        value={username}
                        autoComplete="username"
                        onChange={(e) => setUsername(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '40vw'
                            },
                            '& .MuiOutlinedInput-root': {
                                '&.Mui-focused fieldset': {
                                    borderColor: ' #009900',
                                    borderWidth: 1, // толщина рамки при фокусе
                                },
                            },
                            '& .MuiInputLabel-root': {
                                '&.Mui-focused': {
                                    color: '#009900'
                                }
                            }
                        }}
                    />
                    <TextField
                        label="Пароль"
                        variant="outlined"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '40vw'
                            },
                            '& .MuiOutlinedInput-root': {
                                '&.Mui-focused fieldset': {
                                    borderColor: ' #009900',
                                    borderWidth: 1, // толщина рамки при фокусе
                                },
                            },
                            '& .MuiInputLabel-root': {
                                '&.Mui-focused': {
                                    color: '#009900'
                                }
                            }
                        }}
                    />

                    <Button
                        disabled={(username.length <= 0) || (password.length <= 0)}
                        type="submit"
                        className="form-button"
                        sx={{width: '20vw'}}
                    >Войти</Button>

                    <p style={{margin: 0, textAlign: 'center'}}>Еще нет аккаунта? <a className="redirect-link"
                                                                                     onClick={() => navigate('/register')}>Начать
                        каршерить</a></p>

                </Box>

                <img src={Djiga} alt="car" className="car-image"/>

                {/*<Notification*/}
                {/*    openCondition={error}*/}
                {/*    onNotificationClose={handleNotificationCLose}*/}
                {/*    severity="error"*/}
                {/*    responseText="Error with login! Check credentials and try again"*/}
                {/*    />*/}

            </div>
    )
}

export default LoginForm;