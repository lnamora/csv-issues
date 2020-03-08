import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import { Link } from 'react-router-dom';

class SalesList extends Component {

    constructor(props) {
        super(props);
        this.state = {groups: [], isLoading: true};
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('myapp/load/RegistroVentas1.csv')
            .then(response => response.json())
            .then(data => this.setState({groups: data, isLoading: false}));
    }

    render() {
        const {groups, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const salesList = groups.map(group => {
            return <tr key={group.columnName}>
                <td style={{whiteSpace: 'nowrap'}}>{group.columnName}</td>
                <td style={{whiteSpace: 'nowrap'}}>{group.totalCount}</td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <h3>My sales stats</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Column</th>
                            <th width="20%">count</th>
                        </tr>
                        </thead>
                        <tbody>
                        {salesList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default SalesList;