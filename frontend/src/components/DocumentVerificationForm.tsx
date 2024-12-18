import React, {useEffect, useState} from "react";
import {

    Box,
    Button,
    MenuItem, Select, SelectChangeEvent,
    TextField,
    Typography
} from "@mui/material";
import {useNavigate} from "react-router-dom";
import axiosInstance from "../axiosConfig.ts";
import {DocumentType, VerificationStatuses} from "../interfaces.ts";

const DocumentVerificationForm: React.FC = () => {
    const [documentType, setDocumentType] = useState<string>('')
    const [documentNumber, setDocumentNumber] = useState<string>('')
    const [success, setSuccess] = useState<boolean>(false)
    const [isPending, setIsPending] = useState<boolean>(false)
    const navigate = useNavigate()

    useEffect(() => {
        axiosInstance.get('api/document-verification/my', {
        })
            .then(response => {
                const pendingApplications = response.data.filter((a: { status: VerificationStatuses; }) => a.status === VerificationStatuses.PENDING)
                setIsPending(pendingApplications.length !== 0)
            })
            .catch(error => {
                console.log(error)
            })
    }, []);

    const handelDocumentTypeChange = (event: SelectChangeEvent) => {
        setDocumentType(event.target.value as DocumentType);
    };


    const handleDocuments = async (event: React.FormEvent) => {
        event.preventDefault()
        const documentData = {
            number: documentNumber,
            documentType: documentType,
        }
         axiosInstance.post('api/document-verification', documentData)
                .then((response) => {
                    if (response.status === 201) {
                       setSuccess(true)
                    }

                })
                .catch((error) => {
                    console.log(error)
                })
    }

    return (
        <div className="form-container">

            <Box
                className="base-form"
                component="form"
                onSubmit={handleDocuments}
                sx={{display: 'flex', alignItems: 'center', gap: 3, width: '70vw'}}
            >
                <Typography variant="h5" className="header-text">Подтверждение документов</Typography>

                {isPending &&
                    <Typography variant="h6" color="textPrimary" textAlign='center'>
                        <p style={{marginTop: 0}}>Ваша заявка на верификацию ожидает рассмотрения</p>
                        <a className="redirect-link" onClick={() => navigate('/login')}>На страницу
                            входа</a>
                    </Typography>
                }

                {!isPending &&
                    <div style={{display: 'flex', flexDirection: 'column', gap: 30, alignItems: 'center'}}>


                    <Typography variant="h6" color="textDisabled" textAlign='center'>Для начала использования остался еще один шаг. Вам нужно
                    пройти верификацию документов</Typography>


                <div style={{textAlign: "left"}}>
                    <p style={{marginBottom: 0}}>Тип документа</p>
                    <Select
                        defaultValue={DocumentType.DRIVING_LICENSE}
                        label="Роль"
                        required
                        variant="standard"
                        onChange={handelDocumentTypeChange}
                        sx={{width: '66.5vw',}}

                    >
                        {Object.entries(DocumentType).map((type, index) => (
                            <MenuItem value={type[0]} key={index}>
                                {type[1]}
                            </MenuItem>
                        ))}
                    </Select>

                </div>

                <TextField
                    label="Серия и номер документа"
                    variant="outlined"
                    value={documentNumber}
                    onChange={(e) => setDocumentNumber(e.target.value)}
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
                        disabled={(documentNumber.length <= 0)}
                        type="submit">
                    {'Отправить'}
                </Button>


                {success &&
                    <p style={{margin: 0, textAlign: 'center'}}>Ваша заявка успешно подана и будет рассмотрена в
                        ближайшее время.
                        Вы можете вернуться <a className="redirect-link" onClick={() => navigate('/login')}>на страницу
                            входа</a></p>
                }
                    </div>
                }

            </Box>

        </div>

    )
}

export default DocumentVerificationForm;