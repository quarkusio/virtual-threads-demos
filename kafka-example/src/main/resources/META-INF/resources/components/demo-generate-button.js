import {LitElement, html, css} from 'lit';
import '@vaadin/icon';
import '@vaadin/button';
import '@vaadin/text-field';
import '@vaadin/text-area';
import '@vaadin/form-layout';
import '@vaadin/progress-bar';
import '@vaadin/checkbox';
import '@vaadin/horizontal-layout';
import {until} from 'lit/directives/until.js';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class DemoGenerateButton extends LitElement {
    static styles = css`
      .button {
        cursor: pointer;
      }
      vaadin-text-field {
        width: 50%;
      }
    `;

    static properties = {
        count: {type: Number}
    }

    constructor() {
        super();
        this.count = 2000
    }

    render() {
        return html`
            <vaadin-horizontal-layout theme="spacing padding"
                                      style="align-items: baseline">
                <vaadin-text-field
                        label="Transactions"
                        .value=${this.count}
                        @value-changed=${e => {
                            this.count = e.target.value
                        }}
                        @keyup=${e => e.key === "Enter" && this._generate()}
                        allowed-char-pattern="[0-9]">
                </vaadin-text-field>
                <vaadin-button
                               arial-label="Generate Transactions"
                               @click=${this._generate} class="button primary">
                    Generate
                </vaadin-button>
            </vaadin-horizontal-layout>`;
    }

    _generate() {
        fetch(`/transactions?count=${this.count}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

}

customElements.define('demo-generate-button', DemoGenerateButton);