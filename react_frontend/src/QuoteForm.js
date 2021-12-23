import React, { Component } from 'react'
import axios from 'axios'
import './QuoteForm.css';

export class QuoteForm extends Component {
    constructor(props) {
        super(props);
        this.state = { width: window.innerWidth, height: window.innerHeight, pickupPostcode: '', deliveryPostcode: '', vehicle: '', priceOutput: '' };
        this.handleResize = this.handleResize.bind(this);
        this.pickupPostcodeChanged = this.pickupPostcodeChanged.bind(this);
        this.deliveryPostcodeChanged = this.deliveryPostcodeChanged.bind(this);
        this.vehicleChanged = this.vehicleChanged.bind(this);
        this.handleResponse = this.handleResponse.bind(this);
        this.fetchAnswer = this.fetchAnswer.bind(this);
    }

    handleResize() {
        console.log('resize');
        this.setState({ width: window.innerWidth, height: window.innerHeight })
    }

    componentDidMount() {
        window.addEventListener('resize', this.handleResize);
    }

    componentWillUnmount() {
        window.addEventListener('resize', this.handleResize);
    }

    pickupPostcodeChanged(evt) {
        this.setState({ pickupPostcode : evt.target.value});
    }

    deliveryPostcodeChanged(evt) {
        this.setState({ deliveryPostcode : evt.target.value});
    }

    vehicleChanged(evt) {
        this.setState({ vehicle : evt.target.value});
    }

    handleResponse(data) {
        if (data['vehicle'] == 'invalid') {
            this.setState({ priceOutput: "Invalid vehicle supplied. Try \"bicycle\", \"motorbike\", \"parcel_car\", \"small_van\" or \"large_van\"" });
        } else {
            this.setState({ priceOutput: "£" + data['price'] });
        }
    }

    fetchAnswer() {
        axios.post('/quote', {
                "pickupPostcode" : this.state.pickupPostcode,
                "deliveryPostcode" : this.state.deliveryPostcode,
                "vehicle" : this.state.vehicle
                })
            .then(responseString => this.handleResponse(responseString.data))
    }

    render() {
        const _width = this.state.width;
        const _height = this.state.height;
        return (
            <div style={{ width: _width, height: _height, justifyContent: 'center', justifyItems: 'center', alignContent: 'center', alignItems: 'center', display: 'inline-grid' }}>
                <div id='pickupPostcodeEntry'>
                    Enter pickup postcode:
                    <input value={ this.state.pickupPostcode } type='text' onChange={ this.pickupPostcodeChanged }/>
                </div>
                <div id='deliveryPostcodeEntry'>
                    Enter delivery postcode:
                    <input value={ this.state.deliveryPostcode } type='text' onChange={ this.deliveryPostcodeChanged }/>
                </div>
                <div id='vehicleEntry'>
                    Enter vehicle:
                    <input value={ this.state.vehicle } type='text' onChange={ this.vehicleChanged }/>
                </div>
                <div id='quoteButton'>
                    <button onClick={this.fetchAnswer}>Get Quote</button>
                </div>
                <div id='quoteOutput'>
                    {this.state.priceOutput}
                </div>
            </div>
        );
    }
}