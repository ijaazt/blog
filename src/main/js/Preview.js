import React, {Component} from 'react';

class Preview extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        const {title, author, addedAt, headline} = this.props.content;
        const {onClick} = this.props;
        return (
            <section className={"article-header"}>
                <Header  title={title} firstName={author.firstname} addedAt={addedAt} handleClick={onClick}/>
                <Description headline={headline}/>
            </section>
        )
    }
}

const Header = ({title, firstName, addedAt, handleClick}) =>
    <header className="article-header">
        <h2 className="article-title"><a href="#" onClick={handleClick}>{title}</a></h2>
        <div className="article-meta">By  <strong>{firstName}</strong>, on <strong>{addedAt}</strong></div>
    </header>
const Description = ({headline}) =>
    <div className={"article-description"}>
        {headline}
    </div>

export default Preview;
