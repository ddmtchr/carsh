import React, {useState} from "react";
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    MenuItem, Select, SelectChangeEvent,
    Snackbar,
    TextField,
    Typography
} from "@mui/material";
import {useNavigate} from "react-router-dom";
import axiosInstance from "../axiosConfig.ts";
import {RegisterData, UserRole} from "../interfaces.ts";
import {setUser} from "../stores/userSlice.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../stores/store.ts";

const RegisterForm: React.FC = () => {
    const [username, setUsername] = useState<string>('')
    const [email, setEmail] = useState<string>('')
    const [role, setRole] = useState<string>('')
    const [name, setName] = useState<string>('')
    const [lastName, setLastName] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [phone, setPhone] = useState<string>('')
    const [experience, setExperience] = useState<string>('')
    const [birthDate, setBirthDate] = useState<string>('')
    const [loading, setLoading] = useState(false);
    const [loginError, setLoginError] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [errors, setErrors] = useState<{ password?: string; confirmPassword?: string }>({});
    const navigate = useNavigate()
    const dispatch = useDispatch<AppDispatch>();


    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newPassword = event.target.value;
        setPassword(newPassword);

        // Проверка длины пароля
        if (newPassword.length < 6) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                password: 'Минимум 6 символов!',
            }));
        } else {
            setErrors((prevErrors) => ({ ...prevErrors, password: undefined }));
        }

        // Проверка совпадения с подтверждением
        if (confirmPassword && newPassword !== confirmPassword) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                confirmPassword: "Пароли не совпадают",
            }));
        } else {
            setErrors((prevErrors) => ({ ...prevErrors, confirmPassword: undefined }));
        }
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newConfirmPassword = event.target.value;
        setConfirmPassword(newConfirmPassword);

        // Проверка совпадения с паролем
        if (newConfirmPassword !== password) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                confirmPassword: "Пароли не совпадают",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, confirmPassword: undefined}));
        }
    };

    const handleRoleChange = (event: SelectChangeEvent) => {
        setRole(event.target.value as UserRole);
    };


    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault()
        setLoading(true)
        setLoginError(false)

        const registerData: RegisterData = {
            username: username,
            password: password,
            role: role,
            email: email,
            name: name,
            lastName: lastName,
            phone: phone,
            experience: Number(experience),
            birthDate: birthDate
        }

        if (!errors.password && !errors.confirmPassword) {
            axiosInstance.post('api/auth/register', registerData)
                .then((response) => {
                    if (response.status === 201 || response.status === 200) {
                        localStorage.setItem('accessToken', response.data.jwt)
                        const fetchedUser = {
                            id: response.data.id,
                            role: response.data.role,
                            username: response.data.username
                        }
                        dispatch(setUser(fetchedUser))
                        navigate('/login')
                    }

                })
                .catch((error) => {
                    setLoginError(true)
                    console.log(error)
                })
                .finally(() => setLoading(false))
        }
    }

    const handleNotificationCLose = () => {
        setLoginError(false)
    }

    return (
        <div className="form-container">
            <Box
                className="base-form"
                component="form"
                onSubmit={handleLogin}
                sx={{display: 'flex', alignItems: 'center', gap: 3, width: '70vw'}}
            >
                <Typography variant="h5" className="header-text">Регистрация</Typography>
                <TextField
                    label="Логин"
                    variant="outlined"
                    autoComplete="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    sx={{
                        '& label': {
                            color: 'black',
                        },
                        '& .MuiInputBase-input': {
                            color: 'black',
                            width: '60vw'
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
                    label="Email"
                    variant="outlined"
                    autoComplete="email"
                    value={email}
                    type="email"
                    onChange={(e) => setEmail(e.target.value)}
                    sx={{
                        '& label': {
                            color: 'black',
                        },
                        '& .MuiInputBase-input': {
                            color: 'black',
                            width: '60vw'
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

                <div style={{textAlign: "left"}}>
                    <p style={{marginBottom: 0, fontSize: '15px'}}>Тип доступа</p>
                    <Select
                        defaultValue={UserRole.ROLE_USER}
                        label="Роль"
                        required
                        variant="standard"
                        onChange={handleRoleChange}
                        sx={{width: '66.5vw',}}

                    >
                        {Object.entries(UserRole).map((role, index) => (
                            <MenuItem value={role[0]} key={index}>
                                {role[1]}
                            </MenuItem>
                        ))}
                    </Select>
                </div>
                    <TextField
                        label="Имя"
                        variant="outlined"
                        autoComplete="given-name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                        label="Фамилия"
                        variant="outlined"
                        autoComplete="family-name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                        label="Телефон"
                        variant="outlined"
                        autoComplete="phone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                        label="Стаж"
                        variant="outlined"
                        value={experience}
                        type="number"
                        onChange={(e) => setExperience(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                        label="Дата рождения"
                        variant="outlined"
                        value={birthDate}
                        type="date"
                        focused
                        onChange={(e) => setBirthDate(e.target.value)}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                            },
                        }}
                    />
                    <TextField
                        label="Пароль"
                        variant="outlined"
                        type="password"
                        value={password}
                        autoComplete="new-password"
                        onChange={handlePasswordChange}
                        error={!!errors.password}
                        helperText={errors.password}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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
                        label="Подтверждение пароля"
                        variant="outlined"
                        type="password"
                        value={confirmPassword}
                        autoComplete="new-password"
                        onChange={handleConfirmPasswordChange}
                        error={!!errors.confirmPassword}
                        helperText={errors.confirmPassword}
                        sx={{
                            '& label': {
                                color: 'black',
                            },
                            '& .MuiInputBase-input': {
                                color: 'black',
                                width: '60vw'
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


                    <Button variant="contained"
                            className="form-button"
                            sx={{width: '50vw'}}
                            disabled={!!errors.password || !!errors.confirmPassword || (username.length <= 0) || (password.length <= 0) || (confirmPassword.length <= 0)}
                            type="submit">
                        {loading ? <CircularProgress size={24}/> : 'Зарегистрироваться'}
                    </Button>

                    <p style={{margin: 0, textAlign: 'center'}}>Уже есть аккаунт? <a className="redirect-link"
                                                                                     onClick={() => navigate('/login')}>Войти</a>
                    </p>
            </Box>

            <Snackbar
                open={loginError}
                autoHideDuration={4000}
                onClose={handleNotificationCLose}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert
                    onClose={handleNotificationCLose}
                    severity="error"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    Ошибка при регистрации
                </Alert>
            </Snackbar>
        </div>

    )
}

export default RegisterForm;