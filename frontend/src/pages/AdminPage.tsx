import React, {useEffect, useState} from 'react';
import {
    AppBar,
    Toolbar,
    Typography,
    Button,
    Container,
    Box,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    IconButton
} from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../stores/store.ts";
import {UserRole} from "../interfaces.ts";
import axiosInstance from "../axiosConfig.ts";
import {clearUser} from "../stores/userSlice.ts";
import {useNavigate} from "react-router-dom";
// Тип данных для строки таблицы
interface DocumentData {
    documentType: string;
    id: number;
    number: string | null;
    userId: number;
}



// Компонент Navbar с информацией о пользователе и кнопкой выхода
const Navbar: React.FC = () => {
    const user = useSelector((state: RootState) => state.user);
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate()

    return (
        <AppBar position="static" sx={{backgroundColor: '#009900'}}>
            <Toolbar sx={{justifyContent: 'space-between'}}>
                <Typography variant="h6" sx={{marginRight: 2}}>
                    {/*@ts-ignore*/}
                    {UserRole[user.role]} {user.username}
                </Typography>
                <IconButton color="inherit"
                            onClick={() => {
                                dispatch(clearUser())
                                localStorage.removeItem('accessToken')
                                navigate('/login')
                            }}
                >
                    <LogoutIcon/>
                </IconButton>
            </Toolbar>
        </AppBar>
    )
};

// Компонент таблицы
const AdminPanel: React.FC = () => {

    const [pendingApplications, setPendingApplications] = useState<DocumentData[]>([])

    const fetchPendingApplications = () => {
        axiosInstance.get('api/document-verification/pending')
            .then(response => {
                setPendingApplications(response.data)
            })
            .catch(error => {
                console.log(error)
            })
    }
    useEffect(() => {
        fetchPendingApplications()
        const intervalId = setInterval(fetchPendingApplications, 3000)

        return () => clearInterval(intervalId)
    }, []);
    // Данные для таблицы
    // const data: DocumentData[] = [
    //     {
    //         documentType: 'DRIVING_LICENSE',
    //         id: 2,
    //         number: null,
    //         status: 'PENDING',
    //         userId: 2,
    //     },
    //     // Можно добавить больше данных по аналогии
    // ];

    return (
        <Container>
            <Box sx={{ my: 3 }}>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Тип документа</TableCell>
                                <TableCell>ID заявки</TableCell>
                                <TableCell>Серия и номер</TableCell>
                                <TableCell>ID пользователя</TableCell>
                                <TableCell>Действия</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {pendingApplications.map((row) => (
                                <TableRow key={row.id}>
                                    <TableCell>{row.documentType}</TableCell>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{row.number ? row.number : 'N/A'}</TableCell>
                                    <TableCell>{row.userId}</TableCell>
                                    <TableCell>
                                        <Button
                                            variant="contained"
                                            color="success"
                                            sx={{ mr: 1 }}
                                            title="Утвердить"
                                            onClick={() => {axiosInstance.put(`api/document-verification/${row.id}/verify`)}}
                                        >
                                            ✔
                                        </Button>
                                        <Button
                                            variant="contained"
                                            color="error"
                                            title="Отклонить"
                                            onClick={() => {axiosInstance.put(`api/document-verification/${row.id}/reject`)}}
                                        >
                                            ✖
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </Container>
    );
};

// Главная страница админ-панели
const AdminPage: React.FC = () => {
    return (
        <div>
            <Navbar />
            <AdminPanel />
        </div>
    );
};

export default AdminPage;
