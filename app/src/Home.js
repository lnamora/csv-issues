import React, { Component } from 'react';
import './App.css';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <td>
                    <label htmlFor="fileName">Enter file name </label>
                    <input id="fileName" name="fileName" type="text" />
                </td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"myapp/load/" + fileName}>Edit</Button>
                    </ButtonGroup>
                </td>
            </form>
        );
    }

    render() {
        return (
            <div>
                <Container fluid>
                    <Button color="link"><Link to="/myapp/load/RegistroVentas1.csv">Load Sales files</Link></Button>
                </Container>
            </div>
        );
    }
}

export default Home;